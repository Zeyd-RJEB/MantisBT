<?php

/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['name']) && isset($_POST['status']) && isset($_POST['description'])) {
    
    $status = $_POST['status'];
    $name = $_POST['name'];
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

    // mysql update row with matched name
    $result = mysql_query("UPDATE mantis_project_table SET status = '$status', description = '$description' WHERE name = '$name'");

    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Project successfully updated.";
        
        // echoing JSON response
        echo json_encode($response);
    } else {
        
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>
