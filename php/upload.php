<?php

require 'conectare.php';
require 'update.php';

function getTheme(){
    require 'conectare.php';

    $sql = "SELECT * FROM themes ORDER BY id DESC";
    $response = mysqli_query($conectare, $sql);

    $row = mysqli_fetch_assoc($response);

    return $row['theme'];
}

if($_SERVER['REQUEST_METHOD'] == 'POST'){

	$id = $_POST['id'];
	$photo = $_POST['photo'];
    $theme = getTheme();
    


	//echo $finalPath;
    $sql = "SELECT * FROM idpictures WHERE id='1'";
    $response = mysqli_query($conectare,$sql);
    $row = mysqli_fetch_assoc($response);
    $nr = $row['last_id'];
    $nr++;
    $sql = "UPDATE idpictures SET last_id='$nr' WHERE id='1'";
    $response = mysqli_query($conectare,$sql);

    $path = "images/$nr.jpeg";
    $finalPath = "http://192.168.1.5/".$path;


    $sql = "INSERT INTO pictures (profil_id,link,likes,theme) VALUES ('$id','$finalPath','0','$theme')";
   

    if(mysqli_query($conectare, $sql)) {

    	if(file_put_contents($path, base64_decode($photo))){

            $result['success'] = "1";
            $result['message'] = "success";

            echo json_encode($result);
        } else {
            $result['success'] = "0";
            $result['message'] = "error";

            echo json_encode($result);
        }
    }

    setPosts($id);
}

?>