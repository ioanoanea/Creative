<?php
require 'conectare.php';

$sql = "SELECT * FROM pictures";
$result = mysqli_query($conectare,$sql);

$nr_pictures = 0;
while($row = mysqli_fetch_assoc($result)){
  $pic_user[++$nr_pictures] = $row['profile_id'];
  $pic_name[$nr_pictures] = $row['link'];
  $pic_likes[$nr_pictures] = $row['likes'];
}

for($i = $nr_pictures; $i > 0; $i--){
	
	$sql = "SELECT * FROM users WHERE id='$pic_user[$i]'";
	$result = mysqli_query($conectare,$sql);
	$row = mysqli_fetch_assoc($result);
	 $name = $row['name'];
	 $last_name = $row['last_name'];
	 $username = $row['username'];
	 $profile_image = $row['photo'];

    echo $name." ".$last_name." ".$username." ".$profile_image." ";
	echo $pic_name[$i]." ";
	echo $pic_likes[$i]." ";
	echo '<br>';
}

?>