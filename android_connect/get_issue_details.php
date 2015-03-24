<?php

/*
 * Following code will get single issue details
 * An issue is identified by issue id
 */

// array for JSON response
$response = array();


// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["id"])) {
    $id = intval($_GET['id']);

    // get an issue from bug table
    $result = mysql_query("SELECT * FROM mantis_project_table, mantis_bug_table, mantis_user_table WHERE mantis_bug_table.id = $id AND mantis_bug_table.reporter_id = mantis_user_table.id AND mantis_bug_table.project_id = mantis_project_table.id");

    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {

            $result = mysql_fetch_array($result);
switch ($result["priority"]) {
    case "30":
        $priority="normal";
        break;
		case "40":
        $priority="high";
        break;
    case "50":
        $priority="urgent";
        break;
    case "60":
        $priority="immediate";
		default:
		$priority="low";
        break;
}
switch ($result["severity"]) {
    case "50":
        $severity="minor";
        break;
		case "40":
        $severity="tweak";
        break;
    case "60":
        $severity="major";
        break;
    case "70":
        $severity="crash";
		default:
		$severity="low";
        break;
}
switch ($result["reproducibility"]) {
    case "10":
        $reproducibility="always";
        break;
		case "30":
        $reproducibility="sometimes";
        break;
    case "50":
        $reproducibility="random";
        break;
    case "70":
        $reproducibility="have not tried";
		default:
		$reproducibility="always";
        break;
}
            $issue = array();
            $issue["name"] = $result["name"];
			$issue["username"] = $result["username"];
            $issue["reproducibility"] = $reproducibility;
			$issue["severity"] = $severity;
			$issue["priority"] = $priority;
            $issue["summary"] = $result["summary"];
            // success
            $response["success"] = 1;

            // user node
            $response["issue"] = array();

            array_push($response["issue"], $issue);

            // echoing JSON response
            echo json_encode($response);
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No issue found";

            // echo JSON
            echo json_encode($response);
        }
    } else {
        // no project found
        $response["success"] = 0;
        $response["message"] = "No issue found";

        // echo no issue JSON
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