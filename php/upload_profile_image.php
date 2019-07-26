<?php

require 'conectare.php';
if($_SERVER['REQUEST_METHOD'] == 'POST'){

	$id = $_POST['id'];
	$photo = $_POST['photo'];

    $sql = "SELECT * FROM idpictures WHERE id='2'";
    $response  = mysqli_query($conectare,$sql);
    $row = mysqli_fetch_assoc($response);
    
    $last_id = $row['last_id'] + 1;

    $sql = "UPDATE idpictures SET last_id='$last_id' WHERE id='2'";
    $response = mysqli_query($conectare,$sql);

	$path = "profile_image/$last_id.$id.jpeg";
	$finalPath = "http://192.168.1.5/".$path;

	//echo $finalPath;

    $sql = "UPDATE users SET photo='$finalPath' WHERE id='$id'";

    if(mysqli_query($conectare, $sql)) {

    	if(file_put_contents($path, base64_decode($photo))){

    		$result['success'] = "1";
    		$result['message'] = $finalPath;

    		echo json_encode($result);
    	}
    }
}

?>