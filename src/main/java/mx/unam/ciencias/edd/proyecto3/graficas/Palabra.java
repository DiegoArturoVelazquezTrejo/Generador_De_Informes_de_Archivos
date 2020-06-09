package mx.unam.ciencias.edd.proyecto3.graficas;
import mx.unam.ciencias.edd.*;

/* Clase que nos permitirá ordenar las palabras de acuerdo a sus apariciones */
public class Palabra implements Comparable<Palabra>{
  /* Palabra */
  private String palabra;
  /* Apariciones de la palabra */
  private int apariciones;
  /* Constructor de la clase Palabra */
  public Palabra(String palabra, int apariciones){
    this.palabra = palabra;
    this.apariciones = apariciones;
  }
  /* Métodos getter y setter */
  public int getApariciones(){
    return apariciones;
  }
  public String getPalabra(){
    return palabra;
  }
  /**
  * @return String
  */
  @Override public String toString(){
    return String.format("  '%s' con %d apariciones  ", palabra, apariciones); 
  }
  /* Método para comparar dos palabras (de acuerdo a su número de apariciones )*/
  @Override
  public int compareTo(Palabra palabra){
    return apariciones - palabra.getApariciones();
  }
}
