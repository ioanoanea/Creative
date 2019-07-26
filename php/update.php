<?php
   
   function setPosts($user){

    require 'conectare.php';

     $sql = "SELECT * FROM pictures WHERE profil_id='$user'";
     $result = mysqli_query($conectare, $sql);

     $nr = mysqli_num_rows($result);

    // echo $nr;

     $sql = "UPDATE users SET posts='$nr' WHERE id='$user'";
     $result = mysqli_query($conectare,$sql);
   }

   function setLikes($user){

   	  require 'conectare.php';

      $sql = "SELECT * FROM likes WHERE user='$user'";
      $result = mysqli_query($conectare,$sql);

      $nr = mysqli_num_rows($result);

      $sql = "UPDATE users SET likes='$nr' WHERE id='$user'";
      $result =mysqli_query($conectare,$sql);
   }

   function setUserLikes(){
       require 'conectare.php';

       $sql = "SELECT * FROM pictures";
       $result = mysqli_query($conectare,$sql);

       while ($row = mysqli_fetch_assoc($result)){

         $id = $row['id'];
         $user = $row['profil_id'];
         
         $sql = "UPDATE likes SET user='$user' WHERE img_id='$id'";
         $response = mysqli_query($conectare, $sql);
       }
   }

   function setFollowers($user){
   	require 'conectare.php';

   	$sql = "SELECT * FROM follows WHERE user2='$user'";
   	$result = mysqli_query($conectare,$sql);

   	$nr = mysqli_num_rows($result);

    $sql = "UPDATE users SET followers='$nr' WHERE id='$user'";
    $result = mysqli_query($conectare,$sql);
   
   }

   require 'conectare.php';

   setLikes("12");


?>