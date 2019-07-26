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


   
  if($_SERVER['REQUEST_METHOD'] == 'POST'){
    
    $username = $_POST['username'];
    $search = $_POST['search'];

    $sql = "SELECT * FROM pictures";
    $response = mysqli_query($conectare,$sql);

    $result = array();
    $result['display'] = array();
   
   if($response){

   while($row = mysqli_fetch_assoc($response)){

     $pic_id = $row['profil_id']; //echo $pic_id.'<br>';
     $id = $row['id'];
     $index['id'] = $row['id'];
     $index['user_id'] = $pic_id;
     $index['pic_name'] = $row['link'];
     $index['pic_likes'] = $row['likes'];
     $index['gift'] = $row['gift'];
     $index['theme'] = $row['theme'];
      
      $sql = "SELECT * FROM likes WHERE img_id='$id' AND user_id='$username'";
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