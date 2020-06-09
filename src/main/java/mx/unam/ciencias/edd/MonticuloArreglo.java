package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase para montículos de Dijkstra con arreglos.
 */
public class MonticuloArreglo<T extends ComparableIndexable<T>>
    implements MonticuloDijkstra<T> {

    /* Número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arreglo;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor para montículo de Dijkstra con un arreglo a partir de una
     * colección.
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloArreglo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Construye un nuevo para montículo de Dijkstra con arreglo a partir de un
     * iterable.
     * @param iterable el iterable a partir de la cual construir el montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloArreglo(Iterable<T> iterable, int n) {
        int indice = 0;
        arreglo = nuevoArreglo(n);
        for(T elemento : iterable){
          arreglo[indice] = elemento;
          elemento.setIndice(indice);
          indice++;
        }
        elementos = n;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos == 0 ) throw new IllegalStateException("Empty Heap");
        T minimo = null;
        int indice = 0;
        /* Buscamos al menos un elemento para comparar */
        while(minimo == null && indice < arreglo.length){
          if(arreglo[indice] != null){
            minimo = arreglo[indice];
            break;
          }
          indice++;
        }
        for(int i = 0; i< arreglo.length; i++){
          if(arreglo[i] != null && arreglo[i].compareTo(minimo) < 0)
            minimo = arreglo[i];
        }
        arreglo[minimo.getIndice()] = null;
        minimo.setIndice(-1);
        elementos--;
        return minimo;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del arreglo.
     * @param i el índice del elemento que queremos.
     * @return el <i>i</i>-ésimo elemento del arreglo.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(!indiceValido(i)) throw new NoSuchElementException();
        return arreglo[i];
    }
    /**
    * Necesitamos un método auxiliar para ver si un índice es válido o no
    */
    private boolean indiceValido(int i){
      return (i >= 0 && i < elementos);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Regresa el número de elementos en el montículo.
     * @return el número de elementos en el montículo.
     */
    @Override public int getElementos() {
        return this.elementos;
    }
}
