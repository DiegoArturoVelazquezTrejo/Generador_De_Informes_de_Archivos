package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import mx.unam.ciencias.edd.proyecto3.estructuras_svg.*;
import mx.unam.ciencias.edd.proyecto3.graficas.ManejaPalabras;
import mx.unam.ciencias.edd.proyecto3.graficas.ConteoPalabras;
import java.io.IOException;
import java.io.*;
/**
* Clase que analiza archivo por archivo y genera el cuerpo html de todo el proyecto
*/
public class GeneraIndexHTML{
  /* Variable que tiene el directorio donde se guardará todo el cuerpo del proyecto */
  private File directorioF;
  /* Lista con todos los archivos a analizar */
  private Lista<String> archivos;
  /* Archivos con su información */
  private Archivo[] listaArchivos;
  /**
  * Constructor de la clase GeneraIndexHTML
  * @param Lista<Strin lista con los archivos a analizar
  * @param String directorio
  */
  public GeneraIndexHTML(Lista<String> archivos, String dir){
    this.archivos = archivos;
    this.directorioF = new File(dir);
  }
  /* Método main del proyecto que se encarga de analizar todo */
  public void generaAnalisis(){
    // Si no existe el directorio, creamos el directorio
    if(!directorioF.exists())
      directorioF.mkdir();
    Diccionario<String, Integer> diccionario;
    File file;
    int i = 0;
    listaArchivos = new Archivo[archivos.getLongitud()];
    for(String archivo : archivos){
      file = new File(archivo);
      if(file.exists()){
        diccionario = ConteoPalabras.contarApariciones(archivo);
        if(diccionario != null){
          ManejaPalabras ap = new ManejaPalabras(diccionario, 20);
          try{
            FileWriter mw = new FileWriter(new File(directorioF, "archivo"+i+".html"));
            mw.write(ap.generaHTML());
            mw.close();
          }catch(IOException e){
            System.out.println("No se ha podido escribir el archivo: "+archivo);
          }
          listaArchivos[i] = new Archivo("archivo"+i+".html", diccionario, archivo);
          i++;
        }
      }else
          System.out.println("No existe el archivo: "+archivo);
    }
    escribeIndexHTML();
  }
  /**
  * Método que escribe el cuerpo del Index.html
  */
  public void escribeIndexHTML(){
    try{
      //LectorEntrada.escribirArchivo("archivo"+i+".html", ap.generaHTML());
      FileWriter mw = new FileWriter(new File(directorioF, "index.html"));
      mw.write(generaIndexHTML());
      mw.close();
    }catch(IOException e){
      System.out.println("No se ha podido escribir el archivo: index.html");
    }
  }
  /**
  * Método que genera el index html
  * @return representación en string del html del index.html
  */
  public String generaIndexHTML(){
    String cadena = "<!DOCTYPE html>\n<html lang='en'>\n<title>Analizador de Palabras</title>\n<meta charset='UTF-8'>\n"+
    "<link rel='stylesheet' href='https://www.w3schools.com/w3css/4/w3.css'>\n"+
    "<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Lato'>\n"+
    "<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>\n"+
    "\n<body>\n<div class='w3-container w3-display-container w3-padding-16'>\n<h3 class='w3-wide'><b>Estructuras de Datos</b></h3>\n"+
    "\n</div>\n<div class='w3-content' style='max-width:2000px;'>\n"+
    "\n<div class='w3-container w3-content w3-center w3-padding-64' style='max-width:800px' id='band'>"+
    "\n<h2 class='w3-wide'>Analizador de Palabras</h2>\n<p class='w3-opacity'>\n<i>Ciencias de la Computación: Estructuras de Datos</i></p>"+
    "\n<p class='w3-justify'>Proyecto de estructuras de datos. Analizador de palabras, que realiza el conteo de palabras por archivo"+
    "\ny en número de apariciones. Además, la gráfica que se presenta abajo indica cuando dos archivos tienen palabras en común con una"+
    "\nlongitud de caracteres mayor a 7.</p>\n<p class='w3-justify'>Estructuras de Datos utilizadas:</p>\n<br>"+
    "\n<ol class='w3-wide'><li>Diccionario: Se utilizó para hacer el conteo de apariciones por palabra. </li>"+
    "\n<li>Listas ligadas: Se utilizaron para ordenar las llaves de los diccionarios. </li>"+
    "\n<li>Árboles Rojinegros: Se utilizaron para desplegar las apariciones de palabras. </li>"+
    "\n<li>Árboles Autobalanceables (AVL): Se utilizaron para desplegar las apariciones de palabras. </li>"+
    "\n<li>Conjuntos: Se utilizaron para hallar la palabras en común entre archivos. </li>"+
    "\n<li>Gráficas: Se utilizaron para representar a los archivos que comparten palabras. </li>"+
    "\n</ol></div><div class=' w3-justify w3-container w3-content w3-center w3-padding-64' style='max-width:800px' id='band'>"+
    "\n<h2>Archivos que se analizaron: </h2><ol>";

    String link = "";
    for(Archivo arch : listaArchivos){
      if(arch != null){
        link = "<a "+" href= "+arch.toString()+" style='color:blue;'>"+ arch.getNombre()+" </a>";
        cadena+="\n<li >"+link+" tiene "+arch.getTotalPalabras()+" palabras. </li>";
      }
    }
    cadena+="\n</ol></div><div class='w3-black' id='tour'><div class='w3-container w3-content w3-padding-64' style='max-width:800px'>"+
    "\n<h2 class='w3-wide w3-center'>Relación entre archivos (representada por una gráfica)</h2>"+
    "\n<p class='w3-opacity w3-center'><i>Aquellos que comparten aristas, tienen palabras en común de al menos 7 caracteres.</i></p><br>";
    cadena+=generaGrafica();
    return cadena+="\n</div>\n</div>\n</div>\n</body>\n</html>";
  }
  /**
  * Método para generar la gŕafica de los archivos (aquellos que tienen intersección de palabras)
  * @return String representación en SVG de la gráfica
  */
  public String generaGrafica(){
    String etiqueta = "<p class='w3-opacity w3-center'><i>A continuación mostramos las palabras que comparten los archivos. </i></p><br>";
    DibujaGrafica<String> db = new DibujaGrafica<>();
    for(int i = 0; i < listaArchivos.length; i++)
      if(listaArchivos[i] != null) db.agrega(listaArchivos[i].getNombre());
    for(int i = 0; i < listaArchivos.length - 1; i++){
      for(int j = i + 1; j < listaArchivos.length; j++){
        Conjunto<String> interseccion = listaArchivos[i].comparaArchivo(listaArchivos[j]);
        if(listaArchivos[i] != null && listaArchivos[j] != null && interseccion.getElementos() > 0){
          db.conecta(listaArchivos[i].getNombre(), listaArchivos[j].getNombre());
          etiqueta+="<p class='w3-justify'>Los archivos "+listaArchivos[i].getNombre()+" y "+listaArchivos[j].getNombre()+" tienen en comun las palabras ";
          for(String elemento : interseccion)
            etiqueta+=elemento+" , ";
          etiqueta+="</p>";
        }
      }
    }
    return db.dibujaGrafica()+etiqueta;
  }
}
