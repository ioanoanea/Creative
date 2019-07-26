<?php
   
    require 'conectare.php';

    $id = $_POST['id'];
    $from_user = $_POST['from'];


    $sql = "SELECT * FROM messages WHERE sender='$id' AND receiver='$from_user' OR sender='$from_user' AND receiver='$id'";
    $response = mysqli_query($conectare,$sql);
    
    if($response){
        $result = array();
        $result['display'] = array();
    
        while($row = mysqli_fetch_assoc($response)){
            $index['id'] = $row['id'];
            $index['message'] = $row['message'];
            $index['is_photo'] = $row['is_photo'];
            if($id == $row['sender'])
              $type = 'sent';
            else $type = 'received';
            $index['type'] = $type;

            array_push($result['display'],$index);
        }

        $result['success'] = '1';
        $result['message'] = 'success';
    } else {
    	$result['success'] = '0';
    	$result['message'] = 'error';
    }
    
    echo json_encode($result);

?>