<?php

/*
 * Following code will delete an issue from table
 * An issue is identified by issue id
 */

// array for JSON response
$response = array();

// check for required fields
if (isset($_POST['id'])) {
    $id = intval($_POST['id']);

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();

    // mysql update row with matched name
    $result = mysql_query("DELETE FROM mantis_bug_table WHERE id = $id");
    
    // check if row deleted or not
    if (mysql_affected_rows() > 0) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "issue successfully deleted";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // no product found
        $response["success"] = 0;
        $response["message"] = "No issue found";

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