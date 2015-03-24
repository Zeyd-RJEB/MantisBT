<?php

/*
 * Following code will edit an issue row
 * All issue details are read from HTTP Post Request
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['projectName']) && isset($_POST['id']) && isset($_POST['assignTo']) && isset($_POST['reproducibility'])&&isset($_POST['severity']) && isset($_POST['priority']) && isset($_POST['summary'])&&isset($_POST['description'])) {
    $id = intval($_POST['id']);
    $projectName = $_POST['projectName'];
    $assignTo = $_POST['assignTo'];
	$reproducibility = $_POST['reproducibility'];
	$severity = $_POST['severity'];
	$priority = $_POST['priority'];
	$summary = $_POST['summary'];
    $description = $_POST['description'];
	switch ($reproducibility) {
    case "always":
        $reproducibility=10;
        break;
		case "sometimes":
        $reproducibility=30;
        break;
    case "random":
        $reproducibility=50;
        break;
    case "have not tried":
        $reproducibility=70;
		default:
		$reproducibility=0;
        break;
	}
	switch ($severity) {
    case "minor":
        $severity=50;
        break;
		case "tweak":
        $severity=40;
        break;
    case "major":
        $severity=60;
        break;
    case "crash":
        $severity=70;
		default:
		$severity=0;
        break;
}
	switch ($priority) {
    case "normal":
        $priority=30;
        break;
		case "high":
        $priority=40;
        break;
    case "urgent":
        $priority=50;
        break;
    case "immediate":
        $priority=60;
		default:
		$priority=0;
        break;
}

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
$result = mysql_query("SELECT id FROM mantis_project_table WHERE name='$projectName';") or die(mysql_error());
$project_id=intval(mysql_result($result, 0));
//alert($project_id);
$result = mysql_query("SELECT id FROM mantis_user_table WHERE username='$assignTo';") or die(mysql_error());
$reporter_id=intval(mysql_result($result, 0));
    // mysql inserting a new row
    $result = mysql_query("UPDATE mantis_bug_table SET project_id = '$project_id', reporter_id = '$reporter_id', priority = '$priority', severity = '$severity', reproducibility = '$reproducibility',  summary = '$summary' WHERE id = $id");
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "issue successfully edited.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
        
        // echoing JSON response
        echo json_encode($response);
    }
}else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    // echoing JSON response
    echo json_encode($response);
}
?>