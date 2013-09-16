package topicTracker;

import twitter4j.Status;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Operations {

	
	// Convert a status into a DBObject
	
	public static DBObject dbTweet(Status status) {
		DBObject tweet = new BasicDBObject();
		tweet.put("tweetId", status.getId());
		tweet.put("userId", status.getUser().getId());
		tweet.put("text", status.getText());
		tweet.put("date", status.getCreatedAt());
		
		
		String content=tweet.get("text").toString();
		
	
		

		if (status.getUser().getLocation() != null)
			tweet.put("user_loc", status.getUser().getLocation());

		// If the tweet is GeoLocated we add it
		if (status.getGeoLocation() != null) {
			Double[] geo = { status.getGeoLocation().getLatitude(),
					status.getGeoLocation().getLongitude() };
			tweet.put("loc", geo);
		}

		return tweet;
	}

}
