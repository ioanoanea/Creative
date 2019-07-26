<?php
     
     require 'conectare.php';

  if($_SERVER['REQUEST_METHOD'] == 'POST'){

     $id = $_POST['id'];
     $latitude = $_POST['latitude'];
     $longitude = $_POST['longitude'];


     $sql = "UPDATE users SET latitude=$latitude, longitude=$longitude WHERE id=$id";

     if(mysqli_query($conectare,$sql)){
     	$result['success'] = "1";
     	$result['message'] = "success";
     } else {
     	$result['success'] = "0";
     	$result['message'] = "error";
     }

     echo json_encode($result);

 }

?>