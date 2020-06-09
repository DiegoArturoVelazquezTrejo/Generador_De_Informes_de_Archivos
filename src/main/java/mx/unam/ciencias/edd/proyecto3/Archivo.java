package mx.unam.ciencias.edd.proyecto3;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import java.util.Iterator;

/**
* Clase que tratará a cada archivo como entidad para
* poder generar la gráfica y ver la intersección entre cada archivo
*/
public class Archivo{
  /* Conjunto de palabras para cada archivo */
  private Conjunto<String> palabras;
  /* Total de palabras que tiene cada archivo */
  private int totalPalabras;
  /* Nombre del archivo (referencia html)*/
  private String nombre;
  /* Nombre original */
  private String nombreOriginal;
  /**
  * Constructor de la clase Archivo
  * @param String nombre
  * @param Diccionario<String, Integer> palabras
  */
  public Archivo(String nombre, Diccionario<String, Integer> diccionario, String nombreOriginal){
    this.nombre = nombre;
    this.totalPalabras = 0;
    this.palabras = new Conjunto<String>();
    this.nombreOriginal = nombreOriginal;
    // Tenemos que llenar al conjunto con los elementos del diccionario
    Iterator<String> iteradorLlaves = diccionario.iteradorLlaves();
    while(iteradorLlaves.hasNext()){
      String llave = iteradorLlaves.next();
      totalPalabras+=diccionario.get(llave);
      palabras.agrega(llave);
    }
  }
  /**
  * Método para imprimir la representación en cadena del Archivo
  * @return String
  */
  @Override public String toString(){
    return this.nombre;
  }
  /**
  * Método que regresa el nombre original
  * @return String nombre original
  */
  public String getNombre(){
    return this.nombreOriginal;
  }
  /**
  * Método que regresa la cantidad de palabras que tiene el archivo
  * @return int cantidad de palabras
  */
  public int getTotalPalabras(){
    return this.totalPalabras;
  }
  /**
  * Método que compara dos archivos y te dice si tienen en común palabras con más de 7 dígitos
  * @param Archivo
  * @return Intersección de las palabras con más de 7 caracteres 
  */
  public Conjunto<String> comparaArchivo(Archivo arch){
    Conjunto<String> interseccion = this.palabras.interseccion(arch.palabras);
    if(interseccion.getElementos() == 0) return interseccion;
    for(String elemento : interseccion)
      if(elemento.length() < 7) interseccion.elimina(elemento);
    return interseccion;
  }
}
