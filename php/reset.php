<?php

require 'conectare.php';

function Random_code($n){
	$cod = "0000";
   for($i = 0; $i < $n; $i++)
   	 $cod[$i] = rand()%10;
   	return $cod;
}


if($_SERVER['REQUEST_METHOD'] == 'POST'){
   
   $username = $_POST['username'];
   $cod = Random_code(6);
  
  //verific daca exista username-ul in baza de date
   $sql = "SELECT * FROM users WHERE username='$username'";
   $response = mysqli_query($conectare,$sql);
   $check_username = mysqli_num_rows($response);
   

   //daca exista pun emeilul care ii corespunde in alt tabel inpreuna cu un cod de resetare
   if($check_username){

    	$row = mysqli_fetch_assoc($response);
           
            $email = $row['email'];

        $sql = "INSERT INTO change_password (email,cod) VALUES ('$email','$cod')";
           if(mysqli_query($conectare, $sql)){
            mail($email,"Reset password code","Your reset code is: $cod");

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);

        } else {    

            $result["success"] = "0";
	        $result["message"] = "error";
	        echo json_encode($result);
      
          }


       
   } else{
     //altfel verific daca exista emailul in baza de date 

   $sql = "SELECT * FROM users WHERE email='$username'";
   $response = mysqli_query($conectare,$sql);
   $check_email = mysqli_num_rows($response);

    //daca exista il pun intr-un alt tabel inpreuna cu un cod de resetare
    if($check_email){

    	  $row = mysqli_fetch_assoc($response);

            $email = $row['email'];
          
          $sql = "INSERT INTO change_password (email,cod) VALUES ('$email','$cod')";
           if(mysqli_query($conectare, $sql)){
            mail($email,"Reset password code","Your reset code is: $cod");

            $result['success'] = "1";
            $result['message'] = "success";
            echo json_encode($result);
            mysql_close($conectare);

        } else {     
           
           $result["success"] = "0";
	       $result["message"] = "error";
	       echo json_encode($result);
	       mysql_close($conectare);
         
         }
            
    }else{
       
       //in cazul in care nu exista nici unul transmit acest lucru catre aplicatie
        $result['success'] = "-1";
       	$result['message'] = "error";
        echo json_encode($result);
      
      }

   
  }
    

}

?>