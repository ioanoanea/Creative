<?php

 require 'conectare.php';


  function setGift($gift, $id){

    require 'conectare.php';
   
   $sql = "UPDATE pictures SET gift='$gift' WHERE id='$id'";
   $response = mysqli_query($conectare, $sql);

 }

if($_SERVER['REQUEST_METHOD'] == 'POST'){

 $img_id = $_POST['img_id'];
 $user_id = $_POST['user_id'];

  $sql = "SELECT *FROM likes WHERE img_id='$img_id' AND user_id='$user_id'";
  $response = mysqli_query($conectare,$sql);

  if (!mysqli_num_rows($response)) {
 	$sql = "INSERT INTO likes (img_id,user_id) VALUES ('$img_id','$user_id')";
  
   if(mysqli_query($conectare, $sql)){
     $result['success'] = "1";
     $result['message'] = "success";

    echo json_encode($result);
    
     $sql = "SELECT * FROM pictures WHERE id='$img_id'";
     $response = mysqli_query($conectare,$sql);
     $row = mysqli_fetch_assoc($response);
     $likes = $row['likes'];
     $likes++;
     
     //echo '<br>'+$likes;
     $sql = "UPDATE pictures SET likes='$likes' WHERE id='$img_id'";
     $response = mysqli_query($conectare,$sql);
   } else {
   	 $result['success'] = "0";
     $result['message'] = "error";

     echo json_encode($result);
   }

  } else {  
 	   $result['success'] = "2";
     $result['message'] = "exist";

     echo json_encode($result);

     $sql = "DELETE FROM likes WHERE img_id='$img_id' AND user_id='$user_id'";
     $response = mysqli_query($conectare,$sql);


     $sql = "SELECT * FROM pictures WHERE id='$img_id'";
     $response = mysqli_query($conectare,$sql);
     $row = mysqli_fetch_assoc($response);
     $likes = $row['likes'];
     $likes--;
     
     //echo '<br>'+$likes;
     $sql = "UPDATE pictures SET likes='$likes' WHERE id='$img_id'";
     $response = mysqli_query($conectare,$sql);
 }
  
   $sql = "SELECT * FROM pictures ORDER BY likes DESC";
   $response = mysqli_query($conectare,$sql);

   for($i =1; $i <= 50; $i++){
     $row =mysqli_fetch_assoc($response);
      if($i>3) setGift(4,$row['id']);
      else setGift($i,$row['id']);
   }
 
}


?>
