

// Show all information, defaults to INFO_ALL
<?php
try {
  // open connection to MongoDB server
  $conn = new Mongo('localhost');

  // access database
  $db = $conn->EarthQuake;

  // access collection
  $collection = $db->tweet;

  // execute query
  // retrieve all documents
  $cursor = $collection->find();

  // iterate through the result set
  // print each document
  echo $cursor->count() . ' document(s) found. <br/>';  
  foreach ($cursor as $obj) {
    echo 'Id: ' . $obj['tweetId'] . '<br/>';
    echo 'userId: ' . $obj['userId'] . '<br/>';
    echo 'text: ' . $obj['text'] . '<br/>';
    echo 'loc: ' . $obj['loc'][0] . ' ' . $obj['loc'][1] . '<br/>';	
    echo '<br/>';
  }

  // disconnect from server
  $conn->close();
} catch (MongoConnectionException $e) {
  die('Error connecting to MongoDB server');
} catch (MongoException $e) {
  die('Error: ' . $e->getMessage());
}
?>


