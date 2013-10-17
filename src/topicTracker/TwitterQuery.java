package topicTracker;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.DBObject;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import uk.ac.wlv.sentistrength.SentiStrength;

public class TwitterQuery implements Runnable {

	public final static String OAUTH_CONSUMER_KEY = "2J6YxWjj7zaVt979uoZtA";
	public final static String OAUTH_CONSUMER_SECRET = "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
	public final static String OAUTH_ACCESS_TOKEN = "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";
	public final static String OAUTH_ACCESS_TOKEN_SECRET = "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";

	
	
	
	// The query to retrieve tweets related to an Earthquake
	public final static String QUERY = "temblor OR terremoto OR sismo OR tsunami lang:es";
	
	// The location is centred in Chile with a radius of 2500 KM
	public final static Double LAT_CENT=-36.78552;
	public final static Double LONG_CENT=-69.660645;
	public final static Double RADIUS=2500.0;
	
			
	
	
	
	// Waiting period between queries in minutes
	public final static int WAIT_PERIOD=10;
	
	// Waiting period after a problem with the Twitter API
	public final static int PROBLEM_PERIOD=60;
	
	
	private MongoConnection mongoConnection;
	private SentiStrength sentiStrength;
	
	
	private Twitter twitter;
	private int status; // 0 created, 1 connected to Twitter, -1 problems

	private long sinceId;
	
	public TwitterQuery() {
		// TODO Auto-generated constructor stub
		this.status = 0;
		this.sinceId=0;
	}

	public void setMongoConnection(MongoConnection mC) {
		this.mongoConnection = mC;
	}

	public void setupTwitter() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);

		cb.setDebugEnabled(true).setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
				.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
				.setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
				.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		this.twitter = tf.getInstance();
		this.status = 1;
	}
	
	public void setupSentiStrength(){
		this.sentiStrength = new SentiStrength();
		String sentiParams[] = {"sentidata", "extra/SentiStrength/spanish/", "trinary"};
		this.sentiStrength.initialise(sentiParams);				
		
	}
	
	
	
	// Process the list of Status
	public List<DBObject> processStatus(List<Status> statusList){
		
		List<DBObject> dbObjects=new ArrayList<DBObject>();
		
		for (Status status : statusList) {
			// Only tweets with a geolocation are considered
			if (status.getGeoLocation() != null) {
				System.out.println("@" + status.getUser().getScreenName()
				+ " - " + status.getText()
				+ status.getUser().getLocation());
				
				// Creates TwitterEntry from Status
				TwitterEntry twitterEntry=new TwitterEntry(status);
				// Evaluates the Sentiment
				twitterEntry.evaluateSentiStrength(this.sentiStrength);

				DBObject tweet = twitterEntry.dbTweet();
				dbObjects.add(tweet);				
				

			}

		}
		return dbObjects;
		
	}

	public void getAndSaveTweets() {

		List<DBObject> geoTweets=new ArrayList<DBObject>(); // the list of the processed tweets to be stored
		
		Query query = new Query(QUERY);
		
		//-17.035777,-69.656983
		// -56.535258,-69.660645
		
		
		query.setGeoCode(new GeoLocation(LAT_CENT,LONG_CENT), RADIUS,
				Query.KILOMETERS);
		
		query.setLang("es");
		query.setCount(100);
		query.setSinceId(this.sinceId);
		//query.setGeoCode(location, radius, unit);

		QueryResult result;
		try {
			result = this.twitter.search(query);			
			// To avoid retrieving tweets with a lower Id to the maximum found
			if(result.getMaxId()>this.sinceId)			
				this.sinceId=result.getMaxId();	
			
			
			// process the Status list	and adds the elements		
			geoTweets.addAll(this.processStatus(result.getTweets()));
			
				
			
		while ((query = result.nextQuery()) != null){
			
						
			result = this.twitter.search(query);
			if(result.getMaxId()>this.sinceId)			
				this.sinceId=result.getMaxId();
			
			
			System.out.println(this.sinceId);
			geoTweets.addAll(this.processStatus(result.getTweets()));				
			
		}
		
		this.mongoConnection.insert(geoTweets);

		} catch (TwitterException e) {
			// If there is a problem I set the status to -1
			this.status=-1;				
			e.printStackTrace();
		}

	}

	// 
	public void run() {
		try {
			while (true) {			
				// I get new tweets only if the status is fine
				if(this.status==1){
					this.getAndSaveTweets();
					Thread.sleep(1000 * 60 * WAIT_PERIOD);
					System.out.println("Deperte");					
				}
				
				// If there is a problem I Wait for one Hour and setup twitter again
				else{
					System.out.println("PROBLEM");	
					Thread.sleep(1000 * 60 * PROBLEM_PERIOD);
					this.setupTwitter();
				}
							
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	static public void main(String args[]) {
		MongoConnection mc = new MongoConnection();
		mc.setupMongo();
		
		TwitterQuery tq=new TwitterQuery();
		tq.setMongoConnection(mc);		
		tq.setupSentiStrength();
		
		tq.setupTwitter();
				
		Thread tracker=new Thread(tq);		
		tracker.start();
				
	}



}
