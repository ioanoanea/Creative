<?php

  require 'test_encrypt.php';

  $user1 = $_GET['user1'];
  $user2 = $_GET['user2'];
  $text = $_GET['text'];

 
  $text = encrypt($text);

  echo "from: ".$user1.'<br>'."to: ".$user2.'<br>'."mesage: ".$text;
  
  $text = decrypt($text);

  echo '<br>'.$text;
  
?>