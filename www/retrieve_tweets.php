<?php
try {
	// open connection to MongoDB server
	$conn = new Mongo ( 'localhost' );
	
	// access database
	$db = $conn->EarthQuake;
	
	// access collection
	$collection = $db->geo_tweets;
	
	$cursor;
	
	// if parameter since is used we retrieve recent tweets
	if (isset ( $_GET ['since'] )) {		
		// We use the condition only if since is grater than zero
		if ($_GET ['since'] > 0) {
			$diff = 60 * 60 * $_GET ['since'];
			
			$mongotime = new Mongodate ( time () - $diff );
			
			$condition = array (
					'date' => array (
							'$gt' => $mongotime 
					) 
			);
			
			$cursor = $collection->find ( $condition );
		} 
		// else we retrieve all the tweets
		else {
			$cursor = $collection->find ();
		}
	} 	// if the parameter is not given we also retrieve all of them
	else {
		$cursor = $collection->find ();
	}
	
	$tweets = array ();
	
	foreach ( $cursor as $obj ) {
		$date = date ( 'd-M-Y h:i:s', $obj ['date']->sec );
		$tweet_data = array (
				'text' => $obj ['text'],
				"lat" => $obj ['loc'] [0],
				"long" => $obj ['loc'] [1],
				'label' => $obj ['label'],
				'date' => $date 
		);
		array_push ( $tweets, $tweet_data );
	}
	
	echo json_encode ( array (
			'tweets' => $tweets 
	) );
	
	$conn->close ();
} catch ( MongoConnectionException $e ) {
	die ( 'Error connecting to MongoDB server' );
} catch ( MongoException $e ) {
	die ( 'Error: ' . $e->getMessage () );
}
?>
