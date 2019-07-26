<?php
  
  require 'conectare.php';
  
  //$id = $_POST['id'];

  $sql = "SELECT * FROM themes ORDER BY id DESC";
  $response = mysqli_query($conectare,$sql);

  $row = mysqli_fetch_assoc($response);
  $theme = $row['theme'];

  if($response){
  	$result['success'] = "1";
  	$result['message'] = $theme;
  } else {
  	$result['success'] = "0";
  	$result['message'] = "error";
  }

  echo json_encode($result);

?>