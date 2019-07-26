<?php

  function Send($notification = array()){
      $url = 'https://fcm.googleapis.com/fcm/send';
      $api_key = 'AIzaSyCwuA-tRsBFYs5RDkttHmjy3Vk0Qj2IMVY';

      $headers = array('Authorization: key='.$api_key, 'Content-Type: application/json');


      $fields = array();
      $fields = $notification;

      $ch = curl_init();
      curl_setopt($ch, CURLOPT_URL, $url);
      curl_setopt($ch, CURLOPT_POST, true);
      curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
      curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

      curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
      curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
     
      $result = curl_exec($ch);

      curl_close($ch);

      return json_decode($result);

  }
   

    function sendDataNotification($to,$title,$body,$data = array()){

       $notification = array(
         'to' => $to,
         'notification' => array(
               'title' => $title,
               'body' => $body
          ),
         'data' => $data
        );

       $sent = Send($notification);
       //print_r(Send($notification));

    }
    


    function sendSimpleNotification($to,$title,$body){
      $notification = array(
           'to' => $to,
           'notification' => array(
               'title' => $title,
               'body' => $body
           )
      );  

      $sent = Send($notification);
      //print_r(Send($notification));
    }

    function getUserToken($id){
      require 'conectare.php';

      $sql = "SELECT * FROM users WHERE id='$id'";
      $response = mysqli_query($conectare,$sql);
      $row = mysqli_fetch_assoc($response);

      return $row['token'];
    }



    $data = array(
        'fromuser' => '4',
        'messageid' => '1'
    );

    //sendDataNotification(getUserToken('1'),'test','this is a test',$data);

   // sendSimpleNotification(getUserToken('1'),'test','test2');

   //echo getUserToken('1');

?>