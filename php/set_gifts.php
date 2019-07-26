<?php
   
   require 'conectare.php';

   function setGift($gift,$id){

   	 require 'conectare.php';

     
     $sql = "SELECT * FROM users WHERE id='$id'";
     $response = mysqli_query($conectare,$sql) ;

     $row = mysqli_fetch_assoc($response);

     if($gift == 1){
     	$nr = $row['gold'] + 1;
        $sql = "UPDATE users SET gold='$nr' WHERE id='$id'";
     }
     else if($gift == 2){
     	$nr = $row['silver'] + 1;
        $sql = "UPDATE users SET silver='$nr' WHERE id='$id'";
     }
     else if($gift == 3){
     	$nr = $row['bronze'] + 1;
     	$sql = "UPDATE users SET bronze='$nr' WHERE id='$id'";
     }
      else {
      	$nr = $row['medal'] + 1;
      	$sql = "UPDATE users SET medal='$nr' WHERE id='$id'";
      }

        $response = mysqli_query($conectare,$sql);

    $sql = "INSERT INTO notifications (user,text,type) VALUES ('$id','Congratulations! You have recived a gift','$gift')";
    $res = mysqli_query($conectare,$sql);

   }


function Update(){
   require 'conectare.php';

  $sql = "SELECT * FROM themes ORDER BY id DESC";
  $response = mysqli_query($conectare,$sql);
  $row = mysqli_fetch_assoc($response);
  $theme = $row['theme'];

  $sql = "SELECT * FROM pictures WHERE theme='$theme' ORDER BY likes DESC";
    $response = mysqli_query($conectare,$sql);
 
  $nr = mysqli_num_rows($response);
  if($nr > 50)
    $nr = 50;
  for($i=1; $i<=$nr; $i++){
    $row = mysqli_fetch_assoc($response);

    setGift($i,$row['profil_id']);
  }
}

?>