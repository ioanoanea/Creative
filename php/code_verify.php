<?php
  
  require 'conectare.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

  $email = $_POST['email'];
  $code = $_POST['code'];

  $sql = "SELECT * FROM change_password WHERE email='$email' ORDER BY id DESC"; 
  $response = mysqli_query($conectare,$sql);
  
  $row = mysqli_fetch_assoc($response);
  $get_code = $row['cod'];
  
  if($code == $get_code){
  	$result['success'] = "1";
  	$result['message'] = "success";
  } else {
  	$result['success'] = "0";
  	$result['message'] = "error";
  }
    echo json_encode($result);

}

?>