<?php

/*
 * Following code will list all the users
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all users from mantis_user_table table
$result = mysql_query("SELECT *FROM mantis_user_table;") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // users node
    $response["users"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $user = array();
        $user["id"] = $row["id"];
        $user["username"] = $row["username"];
		$user["email"] = $row["email"];
        
   



        // push single product into final response array
        array_push($response["users"], $user);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no users found
    $response["success"] = 0;
    $response["message"] = "No project found";

    // echo no users JSON
    echo json_encode($response);
}
?>
