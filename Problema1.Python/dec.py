from functools import reduce

archivo = open('texto.txt', 'r')
mensaje = archivo.read().split('\n')
archivo.close()
enteros = mensaje[0].split()

if(int(enteros[0]) == len(mensaje[1])
	and int(enteros[1]) == len(mensaje[2])
	and int(enteros[2]) == len(mensaje[3])):

	codigo = reduce(lambda a,b: a if(a[-1]==b) else a+b , mensaje[3])
	print('Si' if(codigo.find(mensaje[1])) >= 0 else 'No')
	print('Si' if(codigo.find(mensaje[2])) >= 0 else 'No')
