<?php
  
  require 'conectare.php';
  require 'update.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

  $user1 = $_POST['user1'];
  $user2 = $_POST['user2'];
  $action = $_POST['action'];

   if($action == "unfollow")
   	 $sql = "DELETE FROM follows WHERE user1='$user1' AND user2='$user2'";
   else  $sql = "INSERT INTO follows (user1,user2) VALUES ('$user1','$user2')";
  

  if(mysqli_query($conectare,$sql)){
  	$result['success'] = "1";
  	$result['message'] = "success";
  } else {
  	 $result['success'] = "0";
  	 $result['message'] = "error";
  }

   echo json_encode($result);

   setFollowers($user2);

}

?>