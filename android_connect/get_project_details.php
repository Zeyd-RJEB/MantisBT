<?php

/*
 * Following code will get single project details
 * A project is identified by project name
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["name"])) {
    $name = $_GET['name'];

    // get a project from projects table
    $result = mysql_query("SELECT *FROM mantis_project_table WHERE name = $name");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);
switch ($result["status"]) {
    case "10":
        $status="development";
        break;
		case "30":
        $status="release";
        break;
    case "50":
        $status="stable";
        break;
    case "70":
        $status="obsolete";
		default:
		$status="development";
        break;
}
            $project = array();
            $project["name"] = $result["name"];
            $project["status"] = $status;
            $project["description"] = $result["description"];
            // success
            $response["success"] = 1;

            // user node
            $response["project"] = array();

            array_push($response["project"], $project);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No project found";

            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no project found
        $response["success"] = 0;
        $response["message"] = "No project found";

        // echo no users JSON
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