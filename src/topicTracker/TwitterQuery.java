package topicTracker;

import com.mongodb.DBObject;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterQuery implements Runnable {

	public final static String OAUTH_CONSUMER_KEY = "2J6YxWjj7zaVt979uoZtA";
	public final static String OAUTH_CONSUMER_SECRET = "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
	public final static String OAUTH_ACCESS_TOKEN = "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";
	public final static String OAUTH_ACCESS_TOKEN_SECRET = "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";

	// The query to retrieve tweets related to an Earthquake
	public final static String QUERY = "temblor OR terremoto OR sismo OR tsunami lang:es";

	private MongoConnection mongoConnection;
	private Twitter twitter;
	private int status; // 0 created, 1 connected to Twitter, -1 problems

	public TwitterQuery() {
		// TODO Auto-generated constructor stub
		this.status = 0;
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

	public void getAndSaveTweets() {

		Query query = new Query(QUERY);
		query.setCount(100);

		QueryResult result;
		try {
			result = this.twitter.search(query);
			
			for (Status status : result.getTweets()) {

				System.out.println("@" + status.getUser().getScreenName()
						+ " - " + status.getText()
						+ status.getUser().getLocation());

				// Only tweets with a geolocation are considered
				if (status.getGeoLocation() != null) {
					System.out.println("GEO");

					DBObject tweet = Operations.dbTweet(status);
					this.mongoConnection.insert(tweet);

				}

			}

		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 
	public void run() {
		try {
			while (true) {
				this.getAndSaveTweets();
				Thread.sleep(1000 * 60);

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
		tq.setupTwitter();
		
		Thread a=new Thread(tq);
		
		a.start();
		
		//tq.getAndSaveTweets();
		

	}



}
