<?php
 
    require 'conectare.php';

 if($_SERVER['REQUEST_METHOD'] == 'POST'){  

    $id = $_POST['id'];

    $sql = "SELECT * FROM notifications WHERE user='$id'";
    $response = mysqli_query($conectare,$sql);

    if($response){    	
    	$result['success'] = "1";
    	$result['message'] = mysqli_num_rows($response);
    } else {
    	$result['success'] = "0";
    	$result['message'] = "error";
    }

    echo json_encode($result);

}

?>