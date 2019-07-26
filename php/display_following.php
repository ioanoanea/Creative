<?php

require 'conectare.php';


function getUserData($id, $data){
    require 'conectare.php';
  $sql = "SELECT * FROM users WHERE id='$id'";
  $result = mysqli_query($conectare,$sql);

  $row = mysqli_fetch_assoc($result);

  if($data == "name")
    return $row['name'];
  if($data == "last_name")
    return $row['last_name'];
  if($data == "username")
    return $row['username'];
  if($data == "photo")
    return $row['photo'];
  return "0";
}

 function isFollowing($user1,$user2){

 	require 'conectare.php';

 	$sql = "SELECT * FROM follows WHERE user1='$user1' AND user2='$user2'";
    $result = mysqli_query($conectare,$sql);

    return mysqli_num_rows($result);
 }

 function getTheme(){
  require 'conectare.php';

  $sql = "SELECT * FROM themes ORDER BY id DESC";
  $response = mysqli_query($conectare,$sql);

  $row = mysqli_fetch_assoc($response);
  return $row['theme'];
}


   
  if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $user_id = $_POST['user_id'];
    $theme = getTheme();

    $sql = "SELECT * FROM pictures WHERE theme='$theme' ORDER BY id DESC";
    $response = mysqli_query($conectare,$sql);

    $result = array();
    $result['display'] = array();
   
   if($response){

   while($row = mysqli_fetch_assoc($response)){

     $pic_id = $row['profil_id']; //echo $pic_id.'<br>';
     
    if(isFollowing($user_id,$pic_id)){

     $id = $row['id'];
     $index['id'] = $row['id'];
     $index['user_id'] = $pic_id;
     $index['user_id'] = $pic_id;
     $index['pic_name'] = $row['link'];
     $index['pic_likes'] = $row['likes'];
     $index['gift'] = $row['gift'];
      
      $sql = "SELECT *FROM likes WHERE img_id='$id' AND user_id='$user_id'";
      $resp = mysqli_query($conectare,$sql);
      $check = mysqli_num_rows($resp);
     // echo  $check.'<br>';
      $index['user_like'] = $check;
      $index['name'] = getUserData($pic_id,"name");
      $index['last_name'] = getUserData($pic_id,"last_name");
      $index['username'] = getUserData($pic_id,"username");
      $index['profile_picture'] = getUserData($pic_id,"photo");
      array_push($result['display'], $index);
     }
      
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