<?php
//Archivo de texto simulado
$archivo = "5
140 82
89 134
90 110
112 106
88 90";

//separo el archivo en un array
$texto = preg_split('/\n/', $archivo);

//obtengo el numero de rondas
$numRondas = intval($texto[0]);

//inicializo las variables de resultado
$ganador = 0;
$diferiencia = 0;

for ($i=1; $i<=$numRondas; $i++) {
    $jugador1 = intval(preg_split('/\s+/', $texto[$i])[0]);
    $jugador2 = intval(preg_split('/\s+/', $texto[$i])[1]);
    $dif = abs($jugador1 - $jugador2);

    if ($jugador1 > $jugador2 && $dif > $diferiencia) {
        $ganador = 1;
        $diferiencia = $dif;
    } else if ($jugador2 > $jugador1 && $dif > $diferiencia) {
        $ganador = 2;
        $diferiencia = $dif;
    }
}

var_dump($ganador . ' ' . $diferiencia);

?>
