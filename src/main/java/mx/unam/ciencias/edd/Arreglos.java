package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void quickSort(T[] arreglo, Comparator<T> comparador) {
        quickSort(arreglo, 0, arreglo.length-1, comparador);
    }
    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param int cotaInferior del arreglo
     * @param int cotaSuperior del arreglo
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void quickSort(T[] arreglo, int a, int b, Comparator<T> comparador){
      if(b <= a) return;
      int i = a + 1;
      int j = b;
      while(i < j){
        if(comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[a], arreglo[j]) >= 0){
          intercambia(arreglo, i, j);
          i = i + 1;
          j = j - 1;
        }else if(comparador.compare(arreglo[a], arreglo[i]) >= 0)
          i = i + 1;
        else
          j = j - 1;
      }
      if(comparador.compare(arreglo[i], arreglo[a]) > 0)
        i = i - 1;
      intercambia(arreglo, a, i);
      quickSort(arreglo, a, i -1, comparador);
      quickSort(arreglo, i+1, b, comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void selectionSort(T[] arreglo, Comparator<T> comparador) {
        if(arreglo.length == 0) return;
        int indice;
        for(int i = 0; i < arreglo.length; i++){
          indice = i;
          for(int j = i; j < arreglo.length; j++){
            if(comparador.compare(arreglo[indice], arreglo[j]) > 0) indice = j;
          }
          intercambia(arreglo, indice, i);
        }
    }

    /**
    * Método para intercambiar los elementos de un arreglo
    * @param arreglo en donde se intercambiarán los elementos
    * @param int indice del primer elemento
    * @param int indice del segundo elemento
    **/
    private static <T> void intercambia(T[] arreglo, int indice1, int indice2){
      T elemento1 = arreglo[indice1];
      T elemento2 = arreglo[indice2];
      arreglo[indice1] = elemento2;
      arreglo[indice2] = elemento1;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
      if(arreglo.length == 0) return -1;
      return busquedaBinaria(arreglo, 0, arreglo.length -1, elemento, comparador);
    }
    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param arreglo el arreglo dónde buscar.
     * @param cotaInferior cota inferior del arreglo
     * @param cotaSuperior cota superior del arreglo
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int busquedaBinaria(T[] arreglo, int cotaInferior, int cotaSuperior, T elemento, Comparator<T> comparador){
      if(cotaInferior > cotaSuperior ) return -1;
      if(comparador.compare(arreglo[cotaInferior], elemento) == 0) return cotaInferior;
      if(comparador.compare(arreglo[cotaSuperior], elemento) == 0) return cotaSuperior;
      int mitad = (cotaInferior + cotaSuperior)/ 2;
      if(comparador.compare(arreglo[mitad], elemento) == 0) return mitad;
      if(comparador.compare(elemento, arreglo[mitad]) > 0)
        return busquedaBinaria(arreglo, mitad+1, cotaSuperior, elemento, comparador);
      else
        return busquedaBinaria(arreglo, cotaInferior, mitad -1, elemento, comparador);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
