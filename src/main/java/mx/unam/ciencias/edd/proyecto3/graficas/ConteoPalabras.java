package mx.unam.ciencias.edd.proyecto3.graficas;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import mx.unam.ciencias.edd.proyecto3.estructuras_svg.*;
import java.util.NoSuchElementException;
import java.io.IOException;
/**
* Clase para realizar el conteo de las palabras por archivo
* Aquí se le va a pasar un archivo y esta clase realizará el conteo de todas las palabras
* que aparecen en el archivo y generará el diccionario que se pasará como argumento a
* la clase de ManejaPalabras.java
*/
public class ConteoPalabras{
  /* Vamos a declarar el constructor privado de tal forma que esta clase no se pueda instanciar */
  private ConteoPalabras(){}
  /**
  * Método estático para leer las palabras del archivo
  * @param String archivo del cual se leerán las palabras
  * @return Diccionario<String, Integer> diccionario que a la llave le corresponde la palabra y al valor, el número de apariciones
  */
  public static Diccionario<String, Integer> contarApariciones(String archivo){
      try{
        Diccionario<String, Integer> dic =  LectorEntrada.contarPalabras(archivo);
        return dic;
      }catch(IOException e){
        System.out.println("El archivo: "+archivo+" no se ha podido leer");
      }
      return null; 
  }
}
