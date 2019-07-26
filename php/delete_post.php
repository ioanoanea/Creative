<?php
   
   require 'conectare.php';
   require 'update.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

   $id = $_POST['img_id'];

   $sql = "SELECT * FROM pictures WHERE id='$id'";
   $response = mysqli_query($conectare,$sql);

   $row = mysqli_fetch_assoc($response);
   $user_id = $row['profil_id'];

   $sql = "DELETE FROM pictures WHERE id='$id'";

   if(mysqli_query($conectare,$sql)){
   	   $result['success'] = '1';
   	   $result['message'] = 'success';
   } else {
   	    $result['success'] = '0';
   	    $result['message'] = 'error';
   }

    echo json_encode($result);
    setPosts("$user_id");

 }


?>