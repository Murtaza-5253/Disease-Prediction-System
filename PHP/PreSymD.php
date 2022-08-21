<?php
    $SympC=$_REQUEST['SymptomC'];

$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	$result = mysqli_query($con,"select distinct SymptomD from DiseaseDetails where SymptomC='$SympC'");
	
	$response = array();
	
	while($row=mysqli_fetch_array($result))
	{

		array_push($response,array("SymptomD"=>$row[0]));
	}
	print(json_encode(array('result'=>$response)));
	
	
?>