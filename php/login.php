<?php

require 'conectare.php';
require 'encrypt.php';

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $username = $_POST['username'];
    $password = $_POST['password'];

    $password = encrypt($password);

    $sql = "SELECT * FROM users WHERE username='$username'";

    $response = mysqli_query($conectare, $sql);

    $result = array();
    $result['login'] = array();

    if(mysqli_num_rows($response) == 1){

    	$row = mysqli_fetch_assoc($response);
        
        if($password == $row['password']) {
           
            $index['id'] = $row['id'];
            $index['name'] = $row['name'];
            $index['last_name'] = $row['last_name'];
            $index['username'] = $row['username'];
            $index['email'] = $row['email'];
            $index['photo'] = $row['photo'];

            array_push($result['login'], $index);

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

        } else {

        	$result['success'] = "0";
        	$result['message'] = "error";
            echo json_encode($result);

        }

    }  

    else {

        	$result['success'] = "0";
        	$result['message'] = "error";
            echo json_encode($result);

        }
   

}


?>