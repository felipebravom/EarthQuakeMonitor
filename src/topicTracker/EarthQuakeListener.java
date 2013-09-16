package topicTracker;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

// Implement StatusListener to retrieve tweets from the Streaming API

public class EarthQuakeListener implements StatusListener {
	public final static int MAX_TWEETS_ACCUM= 100; // Max number of tweets accumulated before saving	
	private List<Status> tweetAccum; // Accumulates Tweets

	private MongoConnection mongoConnection;
	
	public EarthQuakeListener(MongoConnection mongo) {		
	
		this.mongoConnection=mongo;
		this.tweetAccum=new ArrayList<Status>();
		
		
		
		// TODO Auto-generated constructor stub
	}
	
	public void setMongoConnection(MongoConnection mongo){
		this.mongoConnection=mongo;
	}
	
	

	

	public void onStatus(Status status) {
		
		System.out.println("@" + status.getUser().getScreenName()+" - " + status.getText() +status.getUser().getLocation());
		
		
		System.out.println("Place "+status.getPlace());
		System.out.println("USerPlace"+status.getUser().getLocation());
		System.out.println("Loca"+status.getGeoLocation().toString());
		
		
		
		// Only tweets with a geolocation are considered
		if(status.getGeoLocation()!=null){
			System.out.println("GEO");
			
		    DBObject tweet=Operations.dbTweet(status);	
		    this.mongoConnection.insert(tweet);
		
		}		
		
		
	
//		// check whether the list of tweets is full
//		if(this.tweetAccum.size()==MAX_TWEETS_ACCUM){
//			for(Status st:this.tweetAccum ){
//				
//				
//				
//				
//				System.out.println("@" + st.getUser().getScreenName()
//					+ " - " + st.getText());
//			}
//			
//			this.tweetAccum.clear();
//		}
//		else{
//			this.tweetAccum.add(status);			
//			
//		}
		
		
		
		
		//this.tweets.clear();
		
		
	}

	public void onDeletionNotice(
			StatusDeletionNotice statusDeletionNotice) {
		System.out.println("Got a status deletion notice id:"
				+ statusDeletionNotice.getStatusId());
	}

	public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
		System.out.println("Got track limitation notice:"
				+ numberOfLimitedStatuses);
	}

	public void onScrubGeo(long userId, long upToStatusId) {
		System.out.println("Got scrub_geo event userId:" + userId
				+ " upToStatusId:" + upToStatusId);
	}

	public void onStallWarning(StallWarning warning) {
		System.out.println("Got stall warning:" + warning);
	}

	public void onException(Exception ex) {
		ex.printStackTrace();
	}

}
