package topicTracker;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import twitter4j.Status;
import uk.ac.wlv.sentistrength.SentiStrength;
// Manages the tweet, the sentiment evaluation, and the storing into MongoDB

public class TwitterEntry {
	Status status; //twitter4j Tweet representation
	private int pos;
	private int neg;
	private int neu;

	
	public TwitterEntry(Status status) {
		this.status=status;
		
	}
	
	
	// Evaluates SentiStrength 
	public void evaluateSentiStrength(SentiStrength sentiStrength){
	   String words[]=this.status.getText().split(" ");
		
		String sentence = "";
		
		for (int i = 0; i < words.length; i++) {
			sentence += words[i];
			if (i < words.length - 1) {
				sentence += "+";
			}
		}
		
		

		String result = sentiStrength.computeSentimentScores(sentence);
	
		System.out.println(result);
		
		String[] values = result.split(" ");

		this.pos = Integer.parseInt(values[0]);
		this.neg = Integer.parseInt(values[1]);
		this.neu = Integer.parseInt(values[2]);
		
	}
	
	
	// converts the Entry into a DBObject
	public DBObject dbTweet() {
		DBObject dbTweet = new BasicDBObject();
		dbTweet.put("tweetId", this.status.getId());
		dbTweet.put("userId", this.status.getUser().getId());
		dbTweet.put("text", this.status.getText());
		dbTweet.put("date", this.status.getCreatedAt());
		
		
		

		if (status.getUser().getLocation() != null)
			dbTweet.put("user_loc", status.getUser().getLocation());

		// If the tweet is GeoLocated we add it
		if (status.getGeoLocation() != null) {
			Double[] geo = { status.getGeoLocation().getLatitude(),
					status.getGeoLocation().getLongitude() };
			dbTweet.put("loc", geo);
		}
		
		
		dbTweet.put("pos", pos);
		dbTweet.put("neg", neg);
		dbTweet.put("neu", neu);
		
		
		return dbTweet;
	}
	
	
	
	

}
