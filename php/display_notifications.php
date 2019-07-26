<?php
  
  require 'conectare.php';

  if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $id = $_POST['id'];

    $sql = "SELECT * FROM notifications WHERE user='$id' ORDER BY id DESC";
    $response = mysqli_query($conectare,$sql);

    $result = array();
    $result['display'] = array();
   
   if($response){

   while($row = mysqli_fetch_assoc($response)){

      $index['id'] = $row['id'];
      $index['text'] = $row['text'];
      $index['type'] = $row['type'];

      array_push($result['display'], $index);
    
    }
       
      $result['success'] = "1";
      $result['message'] = "success";
      echo json_encode($result);
    
    } else {
        	$result['success'] = "0";
        	$result['message'] = "error";
            echo json_encode($result);

        }

    
   
}


?>