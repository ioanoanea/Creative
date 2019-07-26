<?php

function Encrypt($password){

$lenght = strlen($password);

$newpassword = '';
$k=0;
for($i = 0; $i < $lenght; $i++){	
   $a = $password[$i]; $b = $password[$i];
   if($password[$i] != ' ')
   for($j = 1; $j <= $lenght; $j++){
     $a++; 
     $l = $lenght % $j;
     while($l){
     	$b++;
     	$l--;
     }
    if($j % 2 == 0)
       $newpassword[$k++] = $a;
    else $newpassword[$k++] = $b;
     }
    else {
    	  $v = 'A';
    	for($c = 1; $c <= $lenght; $c++){
    		$v++;
    		if($lenght % $c == 0)
    			$newpassword[$k++] = $v;
    	}
    }

}
 
  return $newpassword;
}

function Decrypt($password){

  $lenght = strlen($password);
  $newpassword = '';

    return $newpassword;
}


?>