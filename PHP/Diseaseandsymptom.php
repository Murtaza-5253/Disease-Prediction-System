<?php


    if(isset($_REQUEST['Sym1']) && isset($_REQUEST['Symp2']) && isset($_REQUEST['Sym3']) && isset($_REQUEST['Sym4']) && isset($_REQUEST['Sym5']) && isset($_REQUEST['Sym6']) && isset($_REQUEST['Disease']))
    {
    $Sym1 = $_REQUEST['Sym1'];
    $Sym2 = $_REQUEST['Sym2'];
    $Sym3 = $_REQUEST['Sym3'];
    $Sym4 = $_REQUEST['Sym4'];
    $Sym5 = $_REQUEST['Sym5'];
    $Sym6 = $_REQUEST['Sym6'];
    $Disease = $_REQUEST['Disease'];
    }
    else
    {
        $Sym1 = null;
        $Sym2 = null;
        $Sym3 = null;
        $Sym4 = null;
        $Sym5 = null;
        $Sym6 = null;
    }
	 	$con = mysqli_connect("localhost","horizonf_DiseasePred","23March@2001","horizonf_DiseasePred");
    
    
   // $con = mysqli_connect("localhost","id16243539_diseasepred","InfoGalaxy@1234","id16243539_diseasepredictor_db");
    mysqli_query($con,"insert into 	DiseaseandSymptom(Sym1,Sym2,Sym3,Sym4,Sym5,Sym6,Disease) values('$Sym1','$Sym2','$Sym3','$Sym4','$Sym5','$Sym6','$Disease')");
    
    echo "Data Sent Succesfully";

    
    


?>