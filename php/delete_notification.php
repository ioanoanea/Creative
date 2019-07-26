<?php
  
  require 'conectare.php';
 
if($_SERVER['REQUEST_METHOD'] == 'POST'){

  $id = $_POST['id'];

  $sql = "DELETE FROM notifications WHERE id='$id'";
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