<?php
    if(isset($_REQUEST['Disease']))
    {
        $Disease = $_REQUEST['Disease'];
    }
    else
    {
        $Disease=null;
    }
    

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	if($Disease!=null)
	{
	    $result = mysqli_query($con,"select Prec,Medication from Precandmed where DiseaseName='$Disease'");
	
	    $response = array();
	
	    while($row=mysqli_fetch_array($result))
	    {

		    array_push($response,array("Precaution"=>$row[0],"Medication"=>$row[1]));
		   
	    }
	    print(json_encode(array('result'=>$response)));
	}
	else{
	     print(json_encode(array('result'=>"No Data")));
	}
	
    
	
	
	
?>