<?php

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	$ques = mysqli_query($con,"select Ques from Questions where chronic=0");
	$chron = mysqli_query($con,"select Ques from Questions where chronic=1");
	
	$response = array();
	$response1 = array();
	if(mysqli_num_rows($ques)==0 || mysqli_num_rows($chron)==0)
	{
	    echo "No Data Found!";
	}
	else
	{
	    
	
	    while($row1=mysqli_fetch_array($ques))
	    {
		    array_push($response,array("Ques"=>$row1[0]));
	    }
	    
	
	    while($row2=mysqli_fetch_array($chron))
	    {
		    array_push($response1,array("chronic"=>$row2[0]));
		   
	    }
	    print(json_encode(array('ques'=>$response)));
	    print(json_encode(array('chron'=>$response1)));
	}

	
	
?>