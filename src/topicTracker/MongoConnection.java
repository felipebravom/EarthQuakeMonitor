package topicTracker;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

// This class is used to manage the connection with the Database

public class MongoConnection {
	public static final String HOST="localhost"; // database host
	public static final int PORT=27017; // database port 
	public static final String DB_NAME="EarthQuake"; // database name
	public static final String COLLECTION_NAME="geo_tweets";  
	
	
	private MongoClient mongo;
	private DB db;
	private DBCollection table;
	private int status;  // 0 for not initialized, 1 for initialized, -1 for error
	

	

	public MongoConnection() {	
		this.status=0;		
	}
	
	
	// Setup the Connection with the Database
	public void setupMongo() {
		try {
			this.mongo = new MongoClient(HOST, PORT);
			this.db = this.mongo.getDB(DB_NAME);
			this.table = db.getCollection(COLLECTION_NAME);
			this.status=1;
						
			
		} catch (UnknownHostException e) {
			this.status=-1;
			
		}
    }
	
	
	
	public void insert(DBObject object){
		this.table.insert(object);		
	}
	
	public void insert(List<DBObject> objects){
		this.table.insert(objects);		
	}
	
	
	
	public void disconnect(){
		this.mongo.close();
	}
	

}
