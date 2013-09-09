package topicTracker;

import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

// Implement StatusListener to retrieve tweets from the Streaming API

public class EarthQuakeListener implements StatusListener {

	public EarthQuakeListener() {
		// TODO Auto-generated constructor stub
	}

	public void onStatus(Status status) {
		System.out.println("@" + status.getUser().getScreenName()
				+ " - " + status.getText());
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
