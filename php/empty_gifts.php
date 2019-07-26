<?php
  

  require 'conectare.php';


  $sql = "SELECT * FROM users";
  $result = mysqli_query($conectare,$sql);
  
  while($row = mysqli_fetch_assoc($result)){
  	 $id = $row['id'];

  	 $sql = "UPDATE users SET gold='0' , silver='0' , bronze='0' , medal='0' WHERE id='$id'";
  	 $response = mysqli_query($conectare,$sql);
  }

?>