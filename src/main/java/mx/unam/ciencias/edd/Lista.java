package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        public T elemento;
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nodo con un elemento. */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        public Nodo anterior;
        /* El nodo siguiente. */
        public Nodo siguiente;

        /* Construye un nuevo iterador. */
        public Iterador() {
            this.siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
          return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
          if(!hasNext()) throw new NoSuchElementException();
          else{
            T elem = siguiente.elemento;
            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return elem;
          }
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
          if(!hasPrevious()) throw new NoSuchElementException();
          T elem = anterior.elemento;
          siguiente = anterior;
          anterior = anterior.anterior;
          return elem;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
          siguiente = cabeza;
          anterior = null;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return longitud;
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return longitud == 0;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      Nodo nodo = new Nodo(elemento);
      if(esVacia()){
        cabeza = nodo;
        rabo = cabeza;
        cabeza.anterior = null;
        cabeza.siguiente = null;
      }else{
        nodo.anterior = rabo;
        rabo.siguiente = nodo;
        rabo = nodo;
      }
      longitud++;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
      agrega(elemento);
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      Nodo nodo = new Nodo(elemento);
      if(esVacia()){
        cabeza = rabo = nodo;
      }
      else{
        cabeza.anterior = nodo;
        nodo.siguiente = cabeza;
        cabeza = nodo;
      }
      longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      if(i <= 0) agregaInicio(elemento);
      else if(i >= longitud) agregaFinal(elemento);
      else{
        Nodo nodo = getNodo(i);
        Nodo nuevo = new Nodo(elemento);

        nodo.anterior.siguiente = nuevo;
        nuevo.anterior = nodo.anterior;
        nodo.anterior = nuevo;
        nuevo.siguiente = nodo;

        longitud++;
      }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento){
      Nodo nodo = getNodo(indiceDe(elemento));
      eliminaNodo(nodo);
    }

    /**
     * Elimina el nodo que se pasa
     * @param nodo a elminiar de la lista
     */
    private void eliminaNodo(Nodo nodo) {
      if(nodo == null) return;
      if(longitud == 1 && nodo.equals(cabeza)) limpia();
      else if(nodo.equals(cabeza)) eliminaPrimero();
      else if(nodo.equals(rabo)) eliminaUltimo();
      else{
        nodo.anterior.siguiente = nodo.siguiente;
        nodo.siguiente.anterior = nodo.anterior;
        longitud--;
      }
    }

    /**
    * Obtiene el nodo del índice especificado.
    * @param indice de la lista
    * @throws IllegalArgumentException si el índice es menor a 0 o mayor/igual a la longitud
    * @return el nodo en el índice especificado
    */
    private Nodo getNodo(int i) {
      if(i < 0 || i > longitud) return null;
      Nodo nodo = cabeza;
      while(i-- > 0){
        nodo = nodo.siguiente;
      }
      return nodo;
    }
    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
      if(esVacia()) throw new NoSuchElementException();
      T elem = cabeza.elemento;
      if(longitud == 1) limpia();
      else{
        cabeza = cabeza.siguiente;
        cabeza.anterior = null;
        longitud--;
      }
      return elem;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
      if(esVacia()) throw new NoSuchElementException();
      T elem = rabo.elemento;
      if(longitud == 1) limpia();
      else{
        rabo = rabo.anterior;
        rabo.siguiente = null;
        longitud--;
      }
      return elem;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
      return indiceDe(elemento) != -1;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
      Lista<T> reversa = new Lista<T>();
      if(esVacia()) return reversa;
      Nodo nodo = rabo;
      while(nodo != null){
        reversa.agregaFinal(nodo.elemento);
        nodo = nodo.anterior;
      }
      return reversa;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
      Lista<T> lista = new Lista<>();
      if(esVacia()) return lista;
      Nodo nodo = cabeza;
      while(nodo != null){
        lista.agregaFinal(nodo.elemento);
        nodo = nodo.siguiente;
      }
      return lista;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
      cabeza = rabo = null;
      longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
      if(esVacia()) throw new NoSuchElementException();
      else{
        T elem = cabeza.elemento;
        return elem;
      }
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
      if(esVacia()) throw new NoSuchElementException();
      else{
        T elem = rabo.elemento;
        return elem;
      }
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
      if(i < 0 || i >= longitud) throw new ExcepcionIndiceInvalido();
      Nodo nodo = cabeza;
      while(i-- > 0){
        nodo = nodo.siguiente;
      }
      return nodo.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
      if(esVacia()) return -1;
      int i = 0;
      Nodo nodo = cabeza;
      while(nodo != null){
        if(nodo.elemento.equals(elemento)) return i;
        nodo = nodo.siguiente;
        i++;
      }
      return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
      if(longitud == 0) return "[]";
      String s = "[";
      Nodo nodo = cabeza;
      while(nodo != null && !nodo.equals(rabo)){
        s += String.format("%s, ", nodo.elemento);
        nodo = nodo.siguiente;
      }
      s += String.format("%s]", get(longitud-1));
      return s;
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if(lista.getLongitud() != longitud) return false;
        if(lista.esVacia() && this.esVacia()) return true;
        Nodo nodo = cabeza;
        int i = 0;
        while(nodo!= null){
          if(!lista.get(i++).equals(nodo.elemento)) return false;
          nodo = nodo.siguiente;
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
      return mergeSort(copia(), comparador);
    }

    public Lista<T> mergeSort(Lista<T> lista, Comparator<T> comparador){
      if(lista.getLongitud() == 1 || lista.esVacia()) return lista;
      Lista<T> lista1 = new Lista<T>();
      Lista<T> lista2 = new Lista<T>();
      int mitad = lista.getLongitud() / 2;
      while(lista.getLongitud() != mitad){
        lista1.agregaFinal(lista.getPrimero());
        if(lista.getLongitud() != 0)
          lista.eliminaPrimero();
      }
      lista2 = lista.copia();
      return merge(mergeSort(lista1, comparador), mergeSort(lista2, comparador), comparador);
    }
    /**
    * Método merge que concatena a dos listas ya ordenadas
    * @param lista Lista1 que está previamente ordenada
    * @param lista lista2 que está previamente ordenada
    * @return Lista que contiene elementos de ambas listas (ordenada)
    **/
    private Lista<T> merge(Lista<T> lista1, Lista<T> lista2, Comparator<T> comparador){
      Lista<T> listaOrdenada = new Lista<T>();
      while(lista1.cabeza != null && lista2.cabeza != null){
        if(comparador.compare(lista1.cabeza.elemento, lista2.cabeza.elemento) <= 0){
          listaOrdenada.agregaFinal(lista1.cabeza.elemento);
          lista1.eliminaPrimero();
        }else{
          listaOrdenada.agregaFinal(lista2.cabeza.elemento);
          lista2.eliminaPrimero();
        }
      }
      while(lista1.cabeza != null){
        listaOrdenada.agregaFinal(lista1.cabeza.elemento);
        lista1.eliminaPrimero();
      }
      while(lista2.cabeza != null){
        listaOrdenada.agregaFinal(lista2.cabeza.elemento);
        lista2.eliminaPrimero();
      }
      return listaOrdenada;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>> Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
      if(esVacia()) return false;
      Nodo nodo = cabeza;
      while(nodo != null){
        if(comparador.compare(nodo.elemento, elemento) == 0) return true;
        nodo = nodo.siguiente;
      }
      return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>> boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
