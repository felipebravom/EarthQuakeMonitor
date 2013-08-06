package topicTracker;

import java.io.IOException;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Testing {


	public final static String OAUTH_CONSUMER_KEY= "2J6YxWjj7zaVt979uoZtA";
	public final static String OAUTH_CONSUMER_SECRET= "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
	public final static String OAUTH_ACCESS_TOKEN= "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";

	public final static String OAUTH_ACCESS_TOKEN_SECRET= "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";



	public static void main(String[] args) throws TwitterException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Hola Mundo");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(OAUTH_CONSUMER_KEY)
		.setOAuthConsumerSecret(OAUTH_CONSUMER_SECRET)
		.setOAuthAccessToken(OAUTH_ACCESS_TOKEN)
		.setOAuthAccessTokenSecret(OAUTH_ACCESS_TOKEN_SECRET);
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();

		try {
			Query query = new Query("temblor OR terremoto OR sismo");
			query.setCount(100);

			QueryResult result = twitter.search(query);
			for (Status status : result.getTweets()) {
				
		    GeoLocation geo=status.getGeoLocation();
		    
		    if(geo!=null){
		    	System.out.println(geo.toString());
		    	System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
			}
					
		    }
		    

			while(result.hasNext()){

				Query next=result.nextQuery();
				next.setCount(100);
				System.out.println(next.toString());
				result = twitter.search(next);
				
				for (Status status : result.getTweets()) {
				    GeoLocation geo=status.getGeoLocation();
				    
				    if(geo!=null){
				    	System.out.println(geo.toString());
				    	System.out.println("@" + status.getUser().getScreenName() + ":" + status.getText());
					}
				}



			}









		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//			boolean 	hasNext()
		//			test if there is next page
		//			Query 	nextQuery()
		//			Returns a Query instance to fetch next page or null if there is no next page.



	}

}
