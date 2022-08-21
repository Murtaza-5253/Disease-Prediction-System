<?php
    $SympD=$_REQUEST['SymptomD'];

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
	$result = mysqli_query($con,"select distinct SymptomE from DiseaseDetails where SymptomD='$SympD'");
	
	$response = array();
	
	while($row=mysqli_fetch_array($result))
	{

		array_push($response,array("SymptomE"=>$row[0]));
	}
	print(json_encode(array('result'=>$response)));
	
	
?>