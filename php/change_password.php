<?php

  require 'conectare.php';
  require 'encrypt.php';


if($_SERVER['REQUEST_METHOD'] == 'POST'){
  $email = $_POST['email'];
  $password = $_POST['password'];

  $password = encrypt($password);

  $sql = "UPDATE users SET password='$password' WHERE email='$email'";

  if(mysqli_query($conectare,$sql)){
  	$result['success'] = "1";
  	$result['message'] = "success";
  }  else {
  	$result['success'] = "0";
  	$result['message'] = "error";
  }

   echo json_encode($result);
 }  

?>