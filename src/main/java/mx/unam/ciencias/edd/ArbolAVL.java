package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            this.altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return this.altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return String.format("%s %d/%d", elemento.toString(), this.altura, balance(this));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
    * Método que determina si un vértice está balanceado o no.
    * @param VerticeAVL vértice 1
    * @return {-1, 0, 1} si el vértice está balanceado,
              otro número indica que el vértice no está balanceado.
    */
    protected int balance(VerticeAVL v){
      return getAltura(verticeAVL(v.izquierdo)) - getAltura(verticeAVL(v.derecho));
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        rebalancea(verticeAVL(ultimoAgregado));
    }

    /**
    * Método que me permite hacer el cast de vértices
    * @param VerticeArbolBinario vertice
    * @return VerticeAVL vertice casteado
    */
    protected VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice){
      return (VerticeAVL)vertice;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeAVL vertice = verticeAVL(busca(elemento));
        if(vertice == null) return;
        VerticeAVL auxiliar;
        if(vertice.hayIzquierdo()){
          auxiliar = vertice;
          // Obtenemos el vértice mayor del suarbol izquierdo, como en elimina ArbolOrdenado
          vertice = verticeAVL(maximoEnSubArbol(vertice.izquierdo));
          auxiliar.elemento = vertice.elemento;
        }
        // Si sucede esta condición es porque el vértice es una hoja sin hijos
        if(!vertice.hayIzquierdo() && !vertice.hayDerecho())
          eliminaHoja(vertice);
        else
          subeHijo(vertice);
        // Rebalanceamos sobre el padre del vértice
        rebalancea(verticeAVL(vertice.padre));
        elementos--;
    }

    private void subeHijo(VerticeAVL vertice){
      // Esto significa que el vértice tiene un hijo izquierdo
      if(vertice.hayIzquierdo()){
        if(vertice == raiz){
          raiz = vertice.izquierdo;
          vertice.izquierdo.padre = null;
        }
        // El vértice no es la raíz y por ende tiene un padre
        else{
          vertice.izquierdo.padre = vertice.padre;
          if(vertice.padre.izquierdo == vertice)
            vertice.padre.izquierdo = vertice.izquierdo;
          else
            vertice.padre.derecho = vertice.izquierdo;
        }
      }
      // Esto significa que el vértice tiene un hijo derecho
      else if(vertice.hayDerecho()){
        if(vertice == raiz){
          raiz = vertice.derecho;
          vertice.derecho.padre = null;
        }
        // El vértice no es la raíz y por ende tiene un padre
        else{
          vertice.derecho.padre = vertice.padre;
          if(vertice.padre.izquierdo == vertice)
            vertice.padre.izquierdo = vertice.derecho;
          else
            vertice.padre.derecho = vertice.derecho;
        }
      }
    }

    /**
    * Método para eliminar un vértice hoja
    * @param VerticeAVL a eliminar
    */
    private void eliminaHoja(VerticeAVL vertice){
      // Si no hay padre significa que el vértice a eliminar es la raíz
      if(!vertice.hayPadre())
        raiz = ultimoAgregado = null;
      else if(vertice.padre.izquierdo == vertice)
        vertice.padre.izquierdo = null;
      else
        vertice.padre.derecho = null;
    }

    /**
    * Método para rebalancear un árbol binario AVL
    * @param VerticeAVL vértice a rebalancear
    */
    protected void rebalancea(VerticeAVL vertice){
      if(vertice == null) return;

      recalculaAltura(vertice);
      int balanceVertice = balance(vertice);

      if(balanceVertice == -2){
        VerticeAVL q = verticeAVL(vertice.derecho);
        if(balance(q) == 1)
          giraDerechaAVL(q);
        giraIzquierdaAVL(vertice);
      }

      else if(balanceVertice == 2){
        VerticeAVL p = verticeAVL(vertice.izquierdo);
        if(balance(p) == -1)
          giraIzquierdaAVL(p);
        giraDerechaAVL(vertice);
      }

      rebalancea(verticeAVL(vertice.padre));
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }

    /**
    * Método que gira sobre un vértice izquierda y actualiza las alturas.
    * @param VerticeAVL sobre el que se girará
    */
    private void giraIzquierdaAVL(VerticeAVL p){ // Aquí suponemos que p tiene hijo derecho q
      super.giraIzquierda(p);
      recalculaAltura(p);
      recalculaAltura(verticeAVL(p.padre));
      // Aquí vamos a actualizar las alturas de los vértices
      // Recalcular la altura del vértice
      // ecalculando altura de p (sólo ven la altura de sus dos hijos)
      // Y DESPUÉS recalculan la altura de q
      // (la de p que acaban de calcular y la del otro hijo de q)
    }

    /**
    * Método que gira sobre un vértice derecha y actualiza las alturas.
    * @param VerticeAVL sobre el que se girará
    */
    private void giraDerechaAVL(VerticeAVL p){
      super.giraDerecha(p);
      recalculaAltura(p);
      recalculaAltura(verticeAVL(p.padre));
      // Aquí vamos a actualizar las alturas de los vértices
      // Recalcular la altura del vértice
    }

    /**
    * Método que recalcula la altura de un vértice
    * @param VerticeAVL
    */
    private void recalculaAltura(VerticeAVL v){
      v.altura = 1 + Math.max(getAltura(verticeAVL(v.izquierdo)), getAltura(verticeAVL(v.derecho)));
    }

    /**
    * Método que calcula la altura
    * @param VerticeAVL
    * @return altura
    */
    private int getAltura(VerticeAVL v){
      return (v == null) ? -1 : v.altura;
    }
}
