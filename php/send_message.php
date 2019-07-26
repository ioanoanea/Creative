<?php

    require 'conectare.php';
    require 'send_notification.php';


    $from = '4';//$_POST['from'];
    $name = 'test';//$_POST['name'];
    $to = '1';//$_POST['to'];
    $message = 'test';//$_POST['message'];
    $is_photo = '0';//$_POST['is_photo'];
    
    
    $sql = "SELECT * FROM messages ORDER BY id DESC";
    $resp = mysqli_query($conectare,$sql);
    $row = mysqli_fetch_assoc($resp);

    $messageid = $row['id'];
    $messageid ++;



    $sql = "INSERT INTO messages (sender,receiver,message,is_photo) VALUES ('$from','$to','$message','$is_photo')";
    //$response = mysqli_query($conectare,$sql);
    
    if(mysqli_query($conectare,$sql)){
         
               $sql = "DELETE FROM chats WHERE user1='$from' AND user2='$to' OR user1='$to' And user2='$from'";
               $res =mysqli_query($conectare,$sql);

               $sql = "INSERT INTO chats (user1,user2,last_message) VALUES ('$from','$to','$message')";
              //$response = mysqli_query($conectare,$sql);
               if(mysqli_query($conectare,$sql)){
                $result['success'] = '1';
                $result['message'] = 'success'; 

                $data = array(
                    'fromuser' => $from,
                    'messageid' => $messageid,
                    'isphoto' => $is_photo
                );
                  
                sendDataNotification(getUserToken($to),$name,$message,$data);  
               } else {
                $result['success'] = '0';
                $result['message'] = 'error';
               } 

          } else {
                $result['success'] = '0';
                $result['message'] = 'error';
            } 

    echo json_encode($result);

    
?>