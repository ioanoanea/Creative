<?php 

$conectare =  mysqli_connect('localhost', 'root', '', 'creative');

if(!$conectare){
  die ('Conectarea la baza de date nu a reusit');
}

?>