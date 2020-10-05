<?php
 echo "Juego por rondas ". "</br>";
 	 function readFileByLine($theFile, $theLine)
    {
    	$file = new SplFileObject($theFile);
	    $file->seek($theLine);
	    return $file->current();            
    }
	
	$array0 = array_map('intval', explode(' ', readFileByLine("datos.txt", 2)));
	$array1 = array_map('intval', explode(' ', readFileByLine("datos.txt", 3)));
 	
 	function subArray($arrA, $arrB)
    {
        $result = [];
        $elem = count($arrA);

        if ($elem == count($arrB)) {
            for ($i = 0; $i < $elem; $i++) {
                $result[$i] = $arrA[$i] - $arrB[$i];
            }
        }
        return $result;
    }

	$result = subArray($array0, $array1);
	var_dump(max($result));
?>