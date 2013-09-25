<?php
try {
	// open connection to MongoDB server
	$conn = new Mongo('localhost');

	// access database
	$db = $conn->EarthQuake;

	// access collection
	$collection = $db->geo_tweets;

	// execute query
	// retrieve all documents
	$cursor = $collection->find();

	$tweets = array();

	foreach ($cursor as $obj) {
		$date=date('d-M-Y h:i:s', $obj['date']->sec);
		$tweet_data=array('text'=>$obj['text'],"lat"=>$obj['loc'][0],"long"=>$obj['loc'][1],'neu'=>$obj['neu'],'date'=>$date);
		array_push($tweets,$tweet_data);
	}

	echo json_encode(array('tweets'=>$tweets));


	$conn->close();
} catch (MongoConnectionException $e) {
	die('Error connecting to MongoDB server');
} catch (MongoException $e) {
	die('Error: ' . $e->getMessage());
}
?>
