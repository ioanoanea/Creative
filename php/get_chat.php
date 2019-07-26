<?php

    require 'conectare.php';

    function getUserData($id){

        require 'conectare.php';

    	$sql = "SELECT * FROM users WHERE id='$id'";
            $resp = mysqli_query($conectare,$sql);

            $userData = array();

            if(mysqli_num_rows($resp)){
            	$row = mysqli_fetch_assoc($resp);
                
            	$userData['name'] = $row['name'];
            	$userData['last_name'] = $row['last_name'];
            	$userData['photo'] = $row['photo'];
            } else {
            	$userData['name'] = '';
            	$userData['last_name'] = '';
            	$userData['photo'] = '';
            }
           
           return $userData;
    }


    function shortChat($message){
    	$new_message ='abdc';
    	for($i = 0; $i < 25; $i ++)
    		if($message[$i] == "\n")
    		    $new_message[$i] = ' ';	
    		else $new_message[$i] = $message[$i];
    	$new_message = $new_message.'...';
        return $new_message;
    }

    
    $id = $_POST['id'];

    $sql = "SELECT * FROM chats WHERE user1='$id' OR user2='$id' ORDER BY id DESC";

    $response = mysqli_query($conectare,$sql);

    if($response) {

    	$result = array();
    	$result['display_chat'] = array();

       while($row = mysqli_fetch_assoc($response)){

            $index['id'] = $row['id'];
            $index['user1'] = $row['user1'];
            $index['user2'] = $row['user2'];
            if($row['user1'] == $id)
            	$index['seen'] = 1;
            else $index['seen'] = $row['seen'];
            if(strlen($row['last_message']) > 30)
            	$index['last_message'] = shortChat($row['last_message']);
            else $index['last_message'] = $row['last_message'];


            if($row['user1'] == $id)
                $userid = $row['user2'];
            else $userid = $row['user1'];
            
            $userData = array();

            $userData = getUserData($userid);
            
            $index['name'] = $userData['name'];
            $index['last_name'] = $userData['last_name'];
            $index['photo'] = $userData['photo'];

            array_push($result['display_chat'],$index);
       }

       $result['success'] = '1';
       $result['message'] = 'success';
    }  else {
    	$result['success'] = '0';
    	$result['message'] = 'error';
    }


    echo json_encode($result);


?>