from functools import reduce

archivo = open('texto.txt', 'r')

#La entrada al programa es un archivo de texto que consiste en cuatro líneas
#Es un array de 4 porque el archivo de texto consiste en 4 lineas
#pero se agraga el filtro para quedarme con las primera 4 que tengan contenido
texto = list(filter(lambda x: x.strip() != '', archivo.read().split('\n')))[:4]
archivo.close()

#asumo que la primer linea es de tres numeros separado por espacios, y lo convierto a numerico
#La primera línea son tres enteros M1, M2 y N.
M1, M2, N = list(map(lambda x: int(x), texto[0].split()))
#print(N)

#Asigno en estas 3 variables el resto de las lineas
instruccion1, instruccion2, mensaje = texto[1:4]

#valido que el tamaño de los caracteres de las "instrucciones" coincidan en tamaño
if (M1 == len(instruccion1) and M2 == len(instruccion2) and N == len(mensaje)):

	#quito las caracteres consecutivos repetidos
	#Nota: Ninguna instrucción en el libro de instrucciones contiene dos letras iguales seguidas
	mensajeReducido = reduce(lambda a,b: a if(a[-1]==b) else a+b , mensaje)

	#Máximo existe una instrucción escondida por mensaje aunque es posible que no haya ninguna instrucción en el mensaje
	print('Si' if(mensajeReducido.find(instruccion1)) >= 0 else 'No')
	print('Si' if(mensajeReducido.find(instruccion2)) >= 0 else 'No')

else:
	print('No coincide el numero de caracteres')
