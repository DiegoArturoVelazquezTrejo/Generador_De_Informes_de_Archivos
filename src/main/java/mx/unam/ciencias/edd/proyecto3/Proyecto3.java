package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import mx.unam.ciencias.edd.proyecto3.graficas.ManejaPalabras;
import mx.unam.ciencias.edd.proyecto3.graficas.ConteoPalabras;
import java.io.IOException;
import java.io.*;
/**
* Clase principal del proyecto3
*/
public class Proyecto3{
  /* Terminar de manera amigable */
  private static void uso(String mensaje){
    System.out.println(mensaje);
    System.exit(1);
  }

  public static void main(String[] args){
    if(args.length == 0)
      uso("Uso: java -jar proyecto3.jar archivo(s)");
    Lista<String> archivos = new Lista<>();
    String directorio = "";
    // Vamos a agregar todos los argumentos
    for(int i = 0; i < args.length; i++){
      if(!archivos.contiene(args[i]))archivos.agrega(args[i]);
    }
    // Verificamos si el usuario pasó el directorio
    int posicion = archivos.indiceDe("-o");
    if(posicion != -1 && posicion+1 < archivos.getLongitud())
      directorio = archivos.get(posicion + 1);
    else
      uso("No has ingresado el directorio");
    // Eliminamos tanto la bandera como el directorio para solo quedarnos con los archivos a analizar
    archivos.elimina("-o");
    archivos.elimina(directorio);
    if(archivos.getLongitud() == 0)
      uso("Debes ingresar archivos para leer");
    // Le pasamos los archivos que analizará y el directorio en donde guardará el resultado del programa
    GeneraIndexHTML gen = new GeneraIndexHTML(archivos, directorio);
    gen.generaAnalisis();
  }

}
