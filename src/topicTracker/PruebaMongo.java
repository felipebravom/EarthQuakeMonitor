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



	public static void main(String[] args) throws UnknownHostException, TwitterException {
		// TODO Auto-generated method stub


		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setJSONStoreEnabled(true);

		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
		.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
		.setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();




		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("testdb");

		/**** Get collection / table from 'testdb' ****/
		// if collection doesn't exists, MongoDB will create it for you
		DBCollection table = db.getCollection("user");



		Query query = new Query("temblor OR terremoto OR sismo");
		query.setCount(100);

		QueryResult result = twitter.search(query);
		for (Status status : result.getTweets()) {

			GeoLocation geo=status.getGeoLocation();

			if(geo!=null){
				System.out.println(geo.toString());
				System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());

				String json = DataObjectFactory.getRawJSON(status);

				DBObject dbObject = (DBObject)JSON.parse(json);

				table.insert(dbObject);



			}

		}











	}

}
