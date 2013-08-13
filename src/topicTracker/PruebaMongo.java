package topicTracker;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.*;


public class PruebaMongo {

	public final static String OAUTH_CONSUMER_KEY= "2J6YxWjj7zaVt979uoZtA";
	public final static String OAUTH_CONSUMER_SECRET= "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
	public final static String OAUTH_ACCESS_TOKEN= "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";

	public final static String OAUTH_ACCESS_TOKEN_SECRET= "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";


	private MongoClient mongo;
	private DB db;
	private DBCollection table;
	
	public void setupMongo() {
		try {
			this.mongo = new MongoClient("localhost", 27017);
			this.db = this.mongo.getDB("EarthQuake");

			/**** Get collection / table from 'testdb' ****/
			// if collection doesn't exists, MongoDB will create it for you
			this.table = db.getCollection("tweet");
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}


    }
	
	
	public void saveTweets() throws TwitterException{
		
		// Check Mongo Connection
	

			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setJSONStoreEnabled(true);

			cb.setDebugEnabled(true)
			.setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
			.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
			.setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
			.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
			TwitterFactory tf = new TwitterFactory(cb.build());
			Twitter twitter = tf.getInstance();

			Query query = new Query("temblor OR terremoto OR sismo OR tsunami");
			query.setCount(100);
			QueryResult result = twitter.search(query);
			for (Status status : result.getTweets()) {

				GeoLocation geo=status.getGeoLocation();

				if(geo!=null){
					System.out.println(geo.toString());
					System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());

					//					geo.getLatitude();
					//					geo.getLongitude();
					//					
					//					status.getUser();
					//					status.getId();
					//					status.getText();
					//					status.getCreatedAt();
					//					
					//					
					//					BasicDBObject tweet = new BasicDBObject("tweetId", status.getId()).
					//							append("user", status.getUser()).
					//							append("count", 1).
					//							append("loc", new BasicDBObject("x", 203).append("y", 102));
					//
					//					coll.insert(doc);

					String content=status.getText().replaceAll("\"", "");
					

					String json = "{tweetId:" + status.getId() + ",userId:" + status.getUser().getId() + ",text: \"" + content + "\",loc: [ " + geo.getLatitude() + ", " + geo.getLongitude() + "] }";

					System.out.println(json);

					//String json = DataObjectFactory.getRawJSON(status);


					DBObject dbObject = (DBObject)JSON.parse(json);

					this.table.insert(dbObject);



				}


			
		}
			
		}
		
	

	public static void main(String[] args) throws TwitterException  {
		
		PruebaMongo pb=new PruebaMongo();
		pb.setupMongo();
		pb.saveTweets();
		
		
		
		
	}
	
	
		
		




}
