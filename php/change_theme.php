<?php
  
  require 'conectare.php';
  require 'set_gifts.php';
  require 'update.php';
  require 'send_notification.php';

  $theme = $_GET['theme'];
  Update();


  $sql = "INSERT INTO themes (theme) VALUES ('$theme')";

  $result = mysqli_query($conectare,$sql);


  $text = "The new theme is \"$theme\"";

  $sql = "SELECT * FROM users";
  $response = mysqli_query($conectare,$sql);
   
  while($row = mysqli_fetch_assoc($response)){
     $user = $row['id'];
     $sql = "INSERT INTO notifications (user,text,type) VALUES ('$user','$text','theme')";
     $result = mysqli_query($conectare,$sql);  	
     sendSimpleNotification(getUserToken($user),'Theme changed',$text);

     setLikes($row['id']);
     setUserLikes($row['id']);
     setFollowers($row['id']);
  } 

?>