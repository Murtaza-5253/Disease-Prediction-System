<?php

    if(isset($_REQUEST['SymptA']) && !isset($_REQUEST['SymptB']))
    {
         $SymptA = $_REQUEST['SymptA'];
    }
    else if(isset($_REQUEST['SymptA']) && isset($_REQUEST['SymptB']) && !isset($_REQUEST['SymptC']))
    {
        $SymptA = $_REQUEST['SymptA'];
        $SymptB = $_REQUEST['SymptB'];
    }
     else if(isset($_REQUEST['SymptA']) && isset($_REQUEST['SymptB']) && isset($_REQUEST['SymptC']) && !isset($_REQUEST['SymptD']))
    {
        $SymptA = $_REQUEST['SymptA'];
        $SymptB = $_REQUEST['SymptB'];
        $SymptC = $_REQUEST['SymptC'];
    }
     else if(isset($_REQUEST['SymptA']) && isset($_REQUEST['SymptB']) && !isset($_REQUEST['SymptC']) && isset($_REQUEST['SymptD']) && !isset($_REQUEST['SymptE']))
    {
        $SymptA = $_REQUEST['SymptA'];
        $SymptB = $_REQUEST['SymptB'];
        $SymptC = $_REQUEST['SymptC'];
        $SymptD = $_REQUEST['SymptD'];
   
    }
    else if(isset($_REQUEST['SymptA']) && isset($_REQUEST['SymptB']) && isset($_REQUEST['SymptC']) && isset($_REQUEST['SymptD']) && isset($_REQUEST['SymptE']))
    {
        $SymptA = $_REQUEST['SymptA'];
        $SymptB = $_REQUEST['SymptB'];
        $SymptC = $_REQUEST['SymptC'];
        $SymptD = $_REQUEST['SymptD'];
        $SymptE = $_REQUEST['SymptE'];
    }
    else
    {
        $SymptA = null;
        $SymptB = null;
        $SymptC = null;
        $SymptD = null;
        $SymptE = null;
    }
	 $con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	if($SymptA!=null && $SymptB!=null && $SymptC!=null && $SymptD!=null && $SymptE!=null)
	{
    	$result = mysqli_query($con,"select DiseaseName from DiseaseDetails where SymptomA='$SymptA' and SymptomB='$SymptB' and SymptomC='$SymptC' and SymptomD='$SymptD' and SymptomE='$SymptE'");
	
    	$response = array();
	
    	while($row=mysqli_fetch_array($result))
    	{
    		array_push($response,array("DiseaseName"=>$row[0]));
    	}
	}
	else
	{
	    $result = mysqli_query($con,"select DiseaseName from DiseaseDetails");
	
    	$response = array();
	
    	while($row=mysqli_fetch_array($result))
    	{
    		array_push($response,array("DiseaseName"=>$row[0]));
    	}
	}
    	print(json_encode(array('result'=>$response)));

	
	
?>