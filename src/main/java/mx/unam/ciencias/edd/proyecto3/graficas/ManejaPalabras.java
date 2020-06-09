package mx.unam.ciencias.edd.proyecto3.graficas;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import mx.unam.ciencias.edd.proyecto3.estructuras_svg.*;
import java.io.IOException;
import java.util.Iterator;
/**
* Clase para manejar las palabras que se ingresen y de ahí mandarlas a graficar en barras o en pastel
*/
public class ManejaPalabras{
  /* Lista con las palabras */
  private Lista<Palabra> palabras;
  /* total de palabras (equivalente al tamaño del diccionario )*/
  private int total_palabras;
  /* total de apariciones de todas las palabras */
  private int apariciones_total;
  /* Variable que nos indica el porcentaje de palabras que se tomará sobre el total para graficar */
  int porcentajeDePalabras;
  /** Constructor de la clase que recibe datos para graficarlos
  *  @param Diccionario<String, Integer> diccionario con las palabras
  *  @param int porcentaje de palabras que se graficará
  */
  public ManejaPalabras(Diccionario<String, Integer> diccionario, int porcentaje){
    porcentajeDePalabras = porcentaje;
    Lista<Palabra> palabrasTotales = new Lista<>();
    palabras  = new Lista<>();
    // Iteramos sobre las llaves del diccionario para obtener las apariciones por palabra
    Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
    while (iteradorLlaves.hasNext()) {
        String s = iteradorLlaves.next();
        apariciones_total+=diccionario.get(s);  // PORCENTAJE ABSOLUTO ALL 100% DE LAS APARICIONES
        palabrasTotales.agrega(new Palabra(s, diccionario.get(s)));
    }
    total_palabras = diccionario.getElementos();
    /* Ordenamos la lista de palabras de acuerdo a las apariciones que tienen para facilitar su gráfica */
    palabrasTotales = palabrasTotales.mergeSort((a, b) -> b.compareTo(a));
    // Aquí tengo que determinar un corte (N% de las palabras que utilizaremos)
    int corte_palabras = (diccionario.getElementos() > 20) ? (int)Math.ceil((diccionario.getElementos() * porcentajeDePalabras) / 100) : diccionario.getElementos();
    Iterator<Palabra> iterador = palabrasTotales.iterator();
    int i = 0;
    while(iterador.hasNext() && i < corte_palabras){
      Palabra pal = iterador.next();
      //apariciones_total+=pal.getApariciones(); // PORCENTAJE RELATIVO AL 15% DE LAS APARICIONES
      palabras.agrega(pal);
      i++;
    }
  }
  /** Método que imprime la gráfica de pastel
  * @return Representación en cadena (svg) de la gráfica de pastel
  **/
  public String imprimePastel(){
    // Chance aquí tenga que hacer un corte de las palabras (a ver )
    GraficaPastel gp = new GraficaPastel(palabras, apariciones_total);
    return gp.pastel();
  }
  /**
  * @return Representación en cadena (svg) de la gráfica de barras
  **/
  public String imprimeBarras(){
    GraficaBarras gb = new GraficaBarras(palabras, apariciones_total);
    return gb.barras();
  }
  /**
  * Método que genera la representación en cadena de un arbol rojinegro
  * @param Lista de datos
  * @return String representación en svg del arbol rojinegro
  */
  public String obtieneRojinegro(){
    if(palabras == null || palabras.getLongitud() == 0) return "";
    // Tengo que generar la lista de mayores apariciones
    Lista<Integer> mayoresApariciones = new Lista<>();
    Iterator<Palabra> iterador = palabras.iterator();
    int i = 0;
    while(iterador.hasNext() && i < 15){
      Palabra pal = iterador.next();
      mayoresApariciones.agrega(pal.getApariciones());
      i++;
    }
    DibujaArbol ar = new DibujaArbol(mayoresApariciones);
    return ar.dibujaArbol(EstructuraDatos.ARBOLROJINEGRO);
  }
  /**
  * Método que genera la representación en cadena de un arbol avl
  * @param Lista de datos
  * @return String representación en svg del arbol avl
  */
  public String obtieneAVL(){
    if(palabras == null || palabras.getLongitud() == 0) return "";
    // Tengo que generar la lista de mayores apariciones
    Lista<Integer> mayoresApariciones = new Lista<>();
    Iterator<Palabra> iterador = palabras.iterator();
    int i = 0;
    while(iterador.hasNext() && i < 15){
      Palabra pal = iterador.next();
      mayoresApariciones.agrega(pal.getApariciones());
      i++;
    }
    DibujaArbol ab = new DibujaArbol(mayoresApariciones);
    return ab.dibujaArbol(EstructuraDatos.ARBOLAVL);
  }
  /**
  * Método que genera el archivo html en cadena String
  * @return String representación en cadena del reporte en html
  */
  public String generaHTML(){
    String ap = "<!DOCTYPE html>\n<html lang='en'>\n<title>Archivo</title>\n<meta charset='UTF-8'>\n"+
    "\n<link rel='stylesheet' href='https://www.w3schools.com/w3css/4/w3.css'>"+
    "\n<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Lato'>"+
    "\n<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css'>"+
    "\n<body>\n<div class='w3-container w3-display-container w3-padding-16'>\n<h3 class='w3-wide'><b>Estructuras de Datos</b></h3>\n</div>"+
    "\n<div class='w3-content' style='max-width:2000px;'><div class='w3-container w3-content w3-center w3-padding-64' style='max-width:1000px' id='band'>"+
    "\n<h2 class='w3-wide'><a href='index.html'>Analizador de Palabras</a></h2>\n<p class='w3-opacity'><i>Ciencias de la Computación: Estructuras de Datos</i></p>\n";

    ap+= "<div style='margin-left: 20px; margin-right: 20px; border: 3px font-family: Garamond'> Lista de palabras: "+ palabras.toString()+"</div>\n<br><div style = 'display: table-cell'>\n";
    ap+="\n</ol>\n</div>\n<div class='w3-black' id='tour'>\n<div class='w3-container w3-content w3-padding-64' style='max-width:800px'>"+
    "\n<h2 class='w3-wide w3-center'>Porcentaje de apariciones: las palabras con mayor frecuencia</h2>"+
    "\n<p class='w3-opacity w3-center'><i>A continuación se presentan los análisis graficados</i></p><br>";
    ap+=imprimePastel();
    ap+="\n<h2 class='w3-wide w3-center'>Gráfica de Barras</h2>";
    ap+=imprimeBarras();
    ap+="\n<h2 class='w3-wide w3-center'>Árbol Autobalanceable (AVL)</h2>";
    ap+=obtieneAVL();
    ap+="\n<h2 class='w3-wide w3-center'>Árbol Rojinegro</h2>";
    ap+=obtieneRojinegro();
    ap+="\n</div></div></div></body></html>";
    return ap;
  }
}
