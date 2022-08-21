<?php
    $SympB=$_REQUEST['SymptomB'];

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	$result = mysqli_query($con,"select distinct SymptomC from DiseaseDetails where SymptomB='$SympB'");
	
	$response = array();
	
	while($row=mysqli_fetch_array($result))
	{

		array_push($response,array("SymptomC"=>$row[0]));
	}
	print(json_encode(array('result'=>$response)));
	
	
	
?>