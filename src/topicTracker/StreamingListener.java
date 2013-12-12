package topicTracker;

import twitter4j.*;
import twitter4j.auth.AccessToken;

public class StreamingListener {

	public final static String OAUTH_CONSUMER_KEY = "2J6YxWjj7zaVt979uoZtA";
	public final static String OAUTH_CONSUMER_SECRET = "8cIMS0nopUvQ8IVQZIUAx1SE2F56YoIC4PtcEDjn9E";
	public final static String OAUTH_ACCESS_TOKEN = "145084142-F54lBJdshyuLHf43ROpsUqzYt2NIbVqewjLqVdDu";

	public final static String OAUTH_ACCESS_TOKEN_SECRET = "XKCKw6YkZknPXR9A1PgjjiJzQf0MkWBIsz2pobN3VI";

	public StreamingListener() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws TwitterException {

		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();

		twitterStream.setOAuthConsumer(OAUTH_CONSUMER_KEY,
				OAUTH_CONSUMER_SECRET);

		AccessToken accessToken = new AccessToken(OAUTH_ACCESS_TOKEN,
				OAUTH_ACCESS_TOKEN_SECRET);

		twitterStream.setOAuthAccessToken(accessToken);
		
		MongoConnection mongo=new MongoConnection();
		mongo.setupMongo();

		StatusListener listener = new EarthQuakeListener(mongo);
		
		
		
		// Para filtrar 
		
		FilterQuery fq = new FilterQuery();
        String keywords[] = {"terremoto","temblor","sismo","tsunami","telúrico","tiembla","temblando"};

       fq.track(keywords);
        
        
        double[][] bb = {{-57.891497,-81.174317}, {-15.834536,-61.311036 }};
        fq.locations(bb);
        
        
        // Bounding Box: NE -17.507549, -66.417549. SW -56.533779, -109.474930.
        
         // -15.834536,-81.174317  Norte izquierda
         // -15.834536,-61.311036  Norte derecha
        
          //-57.891497,-81.174317   Sur Izquierda
        
        // -57.891497,-61.311036  
        
        
//        double lat = 53.186288;
//        double longitude = -8.043709;
//        double lat1 = lat - 4;
//        double longitude1 = longitude - 8;
//        double lat2 = lat + 4;
//        double longitude2 = longitude + 8;
//
//        double[][] bb = {{longitude1, lat1}, {longitude2, lat2}};
//
//        fq.locations(bb);
  
        
        
        
   
        twitterStream.addListener(listener);
        twitterStream.filter(fq); 
      
		
		
		
		

	}

}
