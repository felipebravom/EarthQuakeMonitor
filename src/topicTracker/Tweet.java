package topicTracker;

import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;

import com.mongodb.DBObject;

import twitter4j.Status;

public class Tweet implements DBObject {
	private Status status; // twitter4j Tweet

	
	public Tweet(Status status) {
		this.status=status;
		
	}

	public boolean containsField(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsKey(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object get(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object put(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putAll(BSONObject arg0) {
		// TODO Auto-generated method stub
		
	}

	public void putAll(Map arg0) {
		// TODO Auto-generated method stub
		
	}

	public Object removeField(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map toMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPartialObject() {
		// TODO Auto-generated method stub
		return false;
	}

	public void markAsPartialObject() {
		// TODO Auto-generated method stub
		
	}

}
