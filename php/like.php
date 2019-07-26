<?php
  
  require 'conectare.php';
  require 'update.php';
  
   function setGift($gift,$id){
      require 'conectare.php';

      $sql = "UPDATE pictures SET gift='$gift' WHERE id='$id'";
      $response = mysqli_query($conectare,$sql);
   }

if($_SERVER['REQUEST_METHOD'] == 'POST'){

  //setez valorile
  $user_id = $_POST['user_id'];
  $img_id = $_POST['img_id'];
  $user = $_POST['user'];
  $action = $_POST['action'];

    
   if($action == "dislike")
   	  $sql = "DELETE FROM likes WHERE img_id='$img_id' AND user_id='$user_id'";
   else 
   	  $sql = "INSERT INTO likes (img_id,user_id,user) VALUES ('$img_id','$user_id','$user')";
   

   if(mysqli_query($conectare,$sql)){
   	 $result["success"] = "1";
	   $result["message"] = "success";
   }  else {
   	$result["success"] = "0";
	  $result["message"] = "error";
   }
   
   echo json_encode($result);

   //actualizez numarul de like-uri de la poza
   $sql = "SELECT * FROM likes WHERE img_id='$img_id'";
   $response = mysqli_query($conectare,$sql);
   $likes = mysqli_num_rows($response);

   $sql = "UPDATE pictures SET likes='$likes' WHERE id='$img_id'";
   $response = mysqli_query($conectare,$sql);

   /*
   //actualizez clasamentul

    $sql = "SELECT * FROM pictures ORDER BY likes DESC";
    $response = mysqli_query($conectare,$sql);

    for($i=1; $i<=50; $i++){
    	$row = mysqli_fetch_assoc($response);
        if($i>3) 
        	setGift(4,$row['id']);

        else
        	 setGift($i,$row['id']);

    }

    setLikes($user);*/
}    

  

?>