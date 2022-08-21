<?php
    $SympA=$_REQUEST['SymptomA'];

	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
	
    $result = mysqli_query($con,"select distinct SymptomB from DiseaseDetails where SymptomA='$SympA'");
	
	$response = array();
	
	while($row=mysqli_fetch_array($result))
	{

		array_push($response,array("SymptomB"=>$row[0]));
	}
	print(json_encode(array('result'=>$response)));
	
	
?>