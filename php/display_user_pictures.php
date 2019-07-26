<?php

require 'conectare.php';


  if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $user_id = $_POST['id'];

    $sql = "SELECT * FROM pictures WHERE profil_id='$user_id' ORDER BY id DESC";
    $response = mysqli_query($conectare,$sql);

    $result = array();
    $result['display'] = array();
   
   if($response){

   while($row = mysqli_fetch_assoc($response)){
 
     $index['id'] = $row['id'];
     $index['pic_name'] = $row['link'];
     $index['pic_likes'] = $row['likes'];
     $index['gift'] = $row['gift'];


    
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