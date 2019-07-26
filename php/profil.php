<?php
  

  require 'conectare.php';

  function isFollowing($user1, $user2){
     
     require 'conectare.php';

     $sql = "SELECT * FROM follows WHERE user1='$user1' AND user2='$user2'";
     $response = mysqli_query($conectare,$sql);

     return mysqli_num_rows($response);
  }
  
 if($_SERVER['REQUEST_METHOD'] == 'POST'){
 
  $user = $_POST['user'];
  $profil = $_POST['profil'];
  
  $sql = "SELECT * FROM users WHERE username='$profil'";
  $response = mysqli_query($conectare,$sql);

   $result = array();
   $result['profil'] = array();

   if(mysqli_num_rows($response) == 1) {
   	$row = mysqli_fetch_assoc($response);

   	  $index['id'] = $row['id'];
      $index['name'] = $row['name'];
      $index['last_name'] = $row['last_name'];
      $index['username'] = $row['username'];
      $index['email'] = $row['email'];
      $index['photo'] = $row['photo'];
      $index['gold'] = $row['gold'];
      $index['silver'] = $row['silver'];
      $index['bronze'] = $row['bronze'];
      $index['medal'] = $row['medal'];
      $index['posts'] = $row['posts'];
      $index['likes'] = $row['likes'];
      $index['followers'] = $row['followers'];
      $index['is_following'] = isFollowing($user,$row['id']);

      array_push($result['profil'],$index);

      $result['success'] = '1';
      $result['message'] = 'success';

      echo json_encode($result);
   }
}

?>