package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            this.color = color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
          return String.format("%s{%s}", color == Color.ROJO ? "R" : "N", elemento.toString());
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
            return (color == vertice.color && super.equals(objeto));

        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
    * Método para hacer cast a los vértices
    */
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> vertice) {
      VerticeRojinegro v = (VerticeRojinegro)vertice;
      return v;
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        if(vertice.getClass() != VerticeRojinegro.class) throw new ClassCastException();
        return verticeRojinegro(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vr = (VerticeRojinegro)super.getUltimoVerticeAgregado();
        vr.color = Color.ROJO;
        rebalanceoAgrega(vr);
    }

    //metodo auxiliar para rebalancear un arbol
    private void rebalanceoAgrega(VerticeRojinegro vertice){
        //caso 1
        if(!vertice.hayPadre()){
            vertice.color= Color.NEGRO;
            return;
        }
        //Caso 2
        VerticeRojinegro p = getPadre(vertice);
        if(esNegro(p))
           return;

        //caso3
        VerticeRojinegro a = getAbuelo(vertice);
        //en esta parte sacamos al tio
        VerticeRojinegro t = getTio(vertice);
        if(esRojo(t)){
            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            rebalanceoAgrega(a);
        }else{
            //caso 4
            VerticeRojinegro aux = p;
            if (a.izquierdo == p && p.derecho == vertice){
                super.giraIzquierda(p);
                //actualiza vertices
                p = vertice;
                vertice = aux;
            }
            if (a.derecho==p && p.izquierdo == vertice){
                super.giraDerecha(p);
                //actualiza verices
                p = vertice;
                vertice = aux;
            }
            //caso 5
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            if (vertice==p.izquierdo){
                super.giraDerecha(a);
            }else{
              super.giraIzquierda(a);
            }
        }

    }

    /**
     * Método auxiliar que nos dice si el vértice es rojo.
     */
    private boolean esRojo(VerticeRojinegro v){
      return(v != null && v.color == Color.ROJO);
    }

    /**
     * Método auxiliar que nos dice si el vértice es negro.
     */
    private boolean esNegro(VerticeRojinegro v){
      return(v == null || v.color == Color.NEGRO);
    }

    /**
     *Método auxiliar para conseguir el padre del vértice.
     */
    private VerticeRojinegro getPadre(VerticeRojinegro v){
      VerticeRojinegro p = verticeRojinegro(v.padre);
      return p;
    }

    /**
     *Método auxiliar para conseguir el abuelo del vértice.
     */
    private VerticeRojinegro getAbuelo(VerticeRojinegro v){
      VerticeRojinegro p = verticeRojinegro(v.padre);
      return getPadre(p);
    }

    /**
     *Método auxiliar para conseguir el tío del vértice.
     */
     private VerticeRojinegro getTio(VerticeRojinegro v){
       VerticeRojinegro a = getAbuelo(v);
       if(v.padre == a.izquierdo)
        return verticeRojinegro(a.derecho);
       else
        return verticeRojinegro(a.izquierdo);
     }

   /**
    *Método auxiliar para conseguir el hermano del vértice.
    */
    private VerticeRojinegro getHermano(VerticeRojinegro v){
       VerticeRojinegro p = verticeRojinegro(v.padre);
       if(v == p.izquierdo)
         return verticeRojinegro(p.derecho);
       else
        return verticeRojinegro(p.izquierdo);
     }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento){

      VerticeRojinegro vertice = verticeRojinegro(busca(elemento));
      // Si no se encuentra el vértice, nos salimos del método
      if(vertice == null) return;
      VerticeRojinegro auxiliar = null;
      if(vertice.hayIzquierdo()){
        auxiliar = vertice;
        // Obtenemos el vértice mayor del suarbol izquierdo, como en elimina ArbolOrdenado
        vertice = verticeRojinegro(maximoEnSubArbol(vertice.izquierdo));
        auxiliar.elemento = vertice.elemento;
        auxiliar = null;
      }
      // Tenemos que ver si es hoja para agregarle un vértice fantasma.
      if(!vertice.hayIzquierdo() && !vertice.hayDerecho()){
        vertice.izquierdo = nuevoVertice(null);
        auxiliar = verticeRojinegro(vertice.izquierdo);
        auxiliar.padre = vertice;
        auxiliar.color = Color.NEGRO;
      }

      VerticeRojinegro hijo = null;
      // El vértice hijo será o el fantasma o será el hijo que tenía el vértice sin ser hoja
       if(vertice.hayIzquierdo())
           hijo = verticeRojinegro(vertice.izquierdo);
       else
           hijo = verticeRojinegro(vertice.derecho);
      // Procedemos a desconectar el padre del hijo
      desconecta(hijo, vertice);

      if(esRojo(hijo) || esRojo(vertice)){
        hijo.color = Color.NEGRO;
      }else{
        hijo.color = Color.NEGRO;
        rebalanceaElimina(hijo);
      }
      mataFantasma(auxiliar);
      elementos--;
    }

    /**
    * Método para rebalancerar después de haber elminado
    * @param VerticeRojinegro sobre del cual balancearemos
    */
    private void rebalanceaElimina(VerticeRojinegro v){
        //Caso 1:
        if(!v.hayPadre()){
          v.color = Color.NEGRO;
          raiz = v;
          return;
        }
        VerticeRojinegro p = getPadre(v);
        VerticeRojinegro h = getHermano(v);

        //Caso 2:
        if(esRojo(h) && esNegro(p)){
          p.color = Color.ROJO;
          h.color = Color.NEGRO;
          if(v == p.izquierdo)
            super.giraIzquierda(p);
          else
            super.giraDerecha(p);
          h = getHermano(v);
          p = getPadre(v);
        }

        VerticeRojinegro hi = verticeRojinegro(h.izquierdo);
        VerticeRojinegro hd = verticeRojinegro(h.derecho);
        //Caso 3:
        if(esNegro(p) && esNegro(h) && esNegro(hi) && esNegro(hd)){
          h.color = Color.ROJO;
          rebalanceaElimina(p);
          return;
        }

        //Caso 4:
        if(esNegro(h) && esNegro(hi) && esNegro(hd) && esRojo(p)){
          h.color = Color.ROJO;
          p.color = Color.NEGRO;
          return;
        }

        //Caso 5:
        if(v == p.izquierdo && esRojo(hi) && esNegro(hd) && esNegro(h)){
          h.color = Color.ROJO;
          hi.color = Color.NEGRO;
          super.giraDerecha(h);
        }else if(v == p.derecho && esNegro(hi) && esRojo(hd) && esNegro(h)){
          h.color = Color.ROJO;
          hd.color = Color.NEGRO;
          super.giraIzquierda(h);
        }

        h = getHermano(v);
        hi = verticeRojinegro(h.izquierdo);
        hd = verticeRojinegro(h.derecho);

        //Caso 6:
        h.color = p.color;
        p.color = Color.NEGRO;
        if(v == p.izquierdo){
          hd.color = Color.NEGRO;
          super.giraIzquierda(p);
        }else{
          hi.color = Color.NEGRO;
          super.giraDerecha(p);
        }
    }

    /**
    *Método auxiliar para sustituir el vértice por uno de sus hijos.
    *@param hijo por el cuál será reemplazado el vértice.
    *@param v vértice que se eliminará.
    */
   private void desconecta(VerticeRojinegro hijo, VerticeRojinegro v){
    // Si el vértice no tiene padre, transferimos todo a la raíz.
    if(!v.hayPadre()){
     raiz = hijo;
     raiz.padre = null;
     return;
    }
    // Aquí sabemos que el abuelo de v será ahora su padre tutor
    hijo.padre = v.padre;
    // En caso en que v sea el hijo izquierdo
    if(v.padre.izquierdo == v){
     if(v.izquierdo == hijo)
       v.padre.izquierdo = v.izquierdo;
     else
       v.padre.izquierdo = v.derecho;
    }
    // En caso en que v sea el hijo derecho
    if(v.padre.derecho == v ){
     if(v.izquierdo == hijo)
       v.padre.derecho = v.izquierdo;
     else
       v.padre.derecho = v.derecho;
    }
   }

    /**
    * Método que elimina al vértice fantasma que se agrego en primera instancia
    * @param VerticeRojinegro vertice a matar
    */
    private void mataFantasma(VerticeRojinegro vertice){
      if(vertice == null) return;
      if(!vertice.hayPadre()){
        this.raiz = null;
        ultimoAgregado = null;
      }else if(vertice.padre.derecho == vertice ){
        vertice.padre.derecho = null;
      }else{
        vertice.padre.izquierdo = null;
      }
    }


    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
