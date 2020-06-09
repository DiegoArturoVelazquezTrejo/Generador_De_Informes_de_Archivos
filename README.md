Generador de Informes de Archivos
====================

Proyecto
De acuerdo a los archivos que se pasen como parámetros, el programa lleva a cabo un
análisis de apariciones por palabra, palabra más comunes e intersección de palabras entre archivos.
Los análisis los deposita en un cuerpo html, con index.html como principal archivo.
-----------------------

### Facultad de Ciencias, Ciencias de la Computación.


$ mvn compile
```

Se necesita del compulador maven para compilar el proyecto.
Ejecución:

```
$ mvn test
...
$ mvn install
...
$ java -jar target/proyecto3 archivos1.extensión archivo2.extensión ... archivoN.extensión -o directorio
```
El directorio es aquél en donde el usuario pretende guardar el resultado del programa.


Estrcuturas de datos que utiliza el proyecto :

* `ArbolAVL.java`,
* `ArbolBinario.java`,
* `ArbolBinarioCompleto.java`,
* `ArbolBinarioOrdenado.java`,
* `ArbolRojinegro.java`,
* `Arreglos.java`,
* `Lista.java`,
* `Diccionario.java`.
* `Conjunto.java`.
