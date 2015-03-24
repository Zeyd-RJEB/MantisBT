<?php

/*
 * Following code will list all the projects
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// get all issues from mantis_bug_table table
$result = mysql_query("SELECT * FROM mantis_bug_table;") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // products node
    $response["issues"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $issue = array();
        $issue["id"] = $row["id"];
        $issue["project_id"] = $row["project_id"];
		$issue["reporter_id"] = $row["reporter_id"];
		$issue["severity"] = $row["reporter_id"];
		$issue["summary"] = $row["summary"];
   



        // push single product into final response array
        array_push($response["issues"], $issue);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no products found
    $response["success"] = 0;
    $response["message"] = "No issues found";

    // echo no users JSON
    echo json_encode($response);
}
?>
