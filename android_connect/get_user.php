<?php

/*
 * Following code will check user's authentication
 */

// check for required fields
if (isset($_POST['username'])) {
    
    $username = $_POST['username'];
    
// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check user from mantis_user_table table
$result = mysql_query("SELECT * FROM mantis_user_table WHERE username='$username';") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // users node
    $response["user"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $user = array();
        
        $user["username"] = $row["username"];
		$user["password"] = $row["password"];
		$user["email"] = $row["email"];
		
   



        // push user into final response array
        array_push($response["user"], $user);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
   
    $response["success"] = 0;
    $response["message"] = "No user found";

    // echo no users JSON
    echo json_encode($response);
}
}
?>