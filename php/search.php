<?php
  
  require 'conectare.php';
   
if($_SERVER['REQUEST_METHOD'] == 'POST'){

  $search = $_POST['search'];

  $sql = "SELECT * FROM users";
  $response = mysqli_query($conectare,$sql);

  $result = array();
  $result['display'] = array();
  if($response){
   while($row = mysqli_fetch_assoc($response)){
  	
     $index['id'] = $row['id'];
     $index['name'] = $row['name'];
     $index['last_name'] = $row['last_name'];
     $index['username'] = $row['username'];
     $index['photo'] = $row['photo'];
     $index['latitude'] = $row['latitude'];
     $index['longitude'] = $row['longitude'];

     array_push($result['display'], $index);

   } 

      $result['success'] = "1";
      $result['message'] = "success";
      echo json_encode($result);

  }
  else {
  	$result['success'] = "0";
  	$result['message'] = "error";
            echo json_encode($result);
  }

}

?>