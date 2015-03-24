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

// get all projects from mantis_project_table table
$result = mysql_query("SELECT *FROM mantis_project_table;") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
    // looping through all results
    // projects node
    $response["projects"] = array();
    
    while ($row = mysql_fetch_array($result)) {
        // temp user array
        $project = array();
        $project["id"] = $row["id"];
        $project["name"] = $row["name"];
        $project["description"] = $row["description"];
   



        // push single project into final response array
        array_push($response["projects"], $project);
    }
    // success
    $response["success"] = 1;

    // echoing JSON response
    echo json_encode($response);
} else {
    // no projects found
    $response["success"] = 0;
    $response["message"] = "No project found";

    // echo no users JSON
    echo json_encode($response);
}
?>
