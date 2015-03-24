<?php

/*
 * Following code will create a new project row
 * All prodjet details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['projectName']) && isset($_POST['status']) && isset($_POST['description'])) {
    
    $projectName = $_POST['projectName'];
    $status = $_POST['status'];
    $description = $_POST['description'];
	switch ($status) {
    case "development":
        $status=10;
        break;
		case "release":
        $status=30;
        break;
    case "stable":
        $status=50;
        break;
    case "obsolete":
        $status=70;
		default:
		$status=0;
        break;
}

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql inserting a new row
    $result = mysql_query("INSERT INTO mantis_project_table(name, status, description) VALUES('$projectName', '$status', '$description')");

    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Project successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>