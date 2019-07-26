<?php
 
 require 'conectare.php'; 
 require 'encrypt.php';
 require 'validate.php'; 

if($_SERVER['REQUEST_METHOD'] == 'POST') {

$name = $_POST['name'];
$last_name = $_POST['last_name'];
$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];
$photo = "https://www.gregorheating.co.uk/wp-content/uploads/2016/06/default-testimonial.png";

//criptare parola

$password = Encrypt($password);


//verificare (daca username-ul sau email-ul nu este deja folosit)

$sql = "SELECT email FROM users WHERE email='$email'";
$response = mysqli_query($conectare, $sql);
$check_email = mysqli_num_rows($response);
$sql = "SELECT username FROM users WHERE username='$username'";
$response = mysqli_query($conectare, $sql);
$check_username = mysqli_num_rows($response);


if(!$check_email && !$check_username){

//inserare in baza de date

$sql = " INSERT INTO users (name,last_name,username,email,password,photo,gold,silver,bronze,medal,posts,likes,followers) VALUES ('$name','$last_name','$username','$email','$password','$photo','0','0','0','0','0','0','0');";


 if(mysqli_query($conectare,$sql)){
	$result["success"] = "1";
	$result["message"] = "success";
	echo json_encode($result);
	mysql_close($conectare);
   } else {
     
    $result["success"] = "0";
	$result["message"] = "error";
	echo json_encode($result);
	mysql_close($conectare);
   }



}
else {
     
    $result["success"] = "2";
	$result["message"] = "error";
	echo json_encode($result);
	mysql_close($conectare);
   }



}
?>