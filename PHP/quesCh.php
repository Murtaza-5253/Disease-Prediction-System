<?php

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	$chron = mysqli_query($con,"select Ques from Questions where chronic=1");
	
	$response = array();

	if(mysqli_num_rows($chron)==0)
	{
	    echo "No Data Found!";
	}
	else
	{
	    while($row2=mysqli_fetch_array($chron))
	    {
		    array_push($response,array("chronic"=>$row2[0]));
		   
	    }
	    print(json_encode(array('chron'=>$response)));
	}

	
	
?>