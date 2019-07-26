<?php
     
     require 'conectare.php';

     $username = $_POST['username'];

     $sql = "SELECT * FROM users WHERE username='$username'";
     $response =mysqli_query($conectare,$sql);

     if($response){
     	$result = array();
        $result['user'] = array();


        while($row = mysqli_fetch_assoc($response)){
        	$index['id'] = $row['id'];
        	$index['name'] = $row['name'];
        	$index['last_name'] = $row['last_name'];
        	$index['username'] = $row['username'];
        	$index['photo'] = $row['photo'];

        	array_push($result['user'],$index);
        }

        $result['success'] = '1';
        $result['message'] = 'success';
     } else {
     	$result['success'] = '0';
     	$result['message'] = 'success';
     } 

     echo json_encode($result);


?>