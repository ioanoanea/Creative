<?php
   
    require 'conectare.php';

    $id = $_POST['id'];
    $token = $_POST['token'];
    
    $sql = "UPDATE users SET token='null' WHERE token='$token'";
    $response  = mysqli_query($conectare,$sql);

    $sql = "UPDATE users SET token='$token' WHERE id=$id";

    if(mysqli_query($conectare,$sql)){
    	$result['success'] = '1';
    	$result['message'] = 'success';
    } else {
    	$result['success'] = '0';
    	$result['message'] = 'error';
    } 
     echo json_encode($result);

?>