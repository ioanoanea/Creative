<?php


   require 'conectare.php';


   $message_id = $_POST['id'];

   $sql = "UPDATE chats SET seen='1' WHERE id='$message_id'";
   
   if(mysqli_query($conectare,$sql)){
   	   $result['success'] = '1';
   	   $result['message'] = 'success';
   }  else {
   	   $result['success'] = '0';
   	   $result['message'] = 'error';
   } 

   echo json_encode($result);

?>