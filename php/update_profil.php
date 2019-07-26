<?php
   
   require 'conectare.php';

  if($_SERVER['REQUEST_METHOD'] == 'POST'){
   
   $id = $_POST['id'];
   $name = $_POST['name'];
   $last_name = $_POST['last_name'];
   $email = $_POST['email'];

   $sql = "SELECT * FROM users WHERE email='$email' AND id!='$id'";
   $response = mysqli_query($conectare,$sql);

   if(!mysqli_num_rows($response)){

   $sql = "UPDATE users SET name='$name', last_name='$last_name', email='$email' WHERE id='$id'";

     if(mysqli_query($conectare,$sql)){
   	   $result['success'] = "1";
   	   $result['message'] = "success";
     } else {
   	   $result['success'] = "0";
   	   $result['message'] = "error";
      }

  } else {
  	 $result['success'] = "2";
  	 $result['message'] = "exist";
  }
   
   echo json_encode($result);
 }  

?>