package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            cola = new Cola<Vertice>();
            if(raiz != null) cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
          if(cola.esVacia()) throw new NoSuchElementException("Cola vacía");
          Vertice v = cola.saca();
          if(v.izquierdo != null) cola.mete(v.izquierdo);
          if(v.derecho != null) cola.mete(v.derecho);
          return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
      if (elemento == null)
            throw new IllegalArgumentException();

        Vertice el = new Vertice(elemento);
        if (raiz == null)
            raiz  = el;
        else {
            //Se hace una referencia a la raiz.
            Vertice aux = raiz;
            Cola<ArbolBinario<T>.Vertice> cola = new Cola<>();
            cola.mete(aux);
            while (!cola.esVacia()) {
                aux = cola.saca();
                if (!aux.hayIzquierdo() || !aux.hayDerecho()) {
                    el.padre = aux;
                    if (!aux.hayIzquierdo())
                        aux.izquierdo = el;
                    else if (!aux.hayDerecho())
                        aux.derecho = el;
                    break;
                }
                cola.mete(aux.izquierdo);
                cola.mete(aux.derecho);
            }
        }
        elementos++;
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      Vertice vertice = vertice(busca(elemento));
      if(vertice == null) return;
      this.elementos--;
      if(this.elementos == 0){
        raiz = null;
        return;
      }
      Vertice ultimo = bfs();
      vertice.elemento = ultimo.elemento;
      Vertice padreUltimo = ultimo.padre;
      if(padreUltimo.hayIzquierdo())
        if(padreUltimo.izquierdo == ultimo) padreUltimo.izquierdo = null;
      else
        if(padreUltimo.derecho == ultimo) padreUltimo.derecho = null;
    }

    /* Método auxiliar que implementa el bfs y regresa el nodo último */
    private Vertice bfs(){
      Cola<Vertice> cola = new Cola<Vertice>();
      cola.mete(this.raiz);
      Vertice v = raiz;
      while(!cola.esVacia()){
        v = cola.saca();
        if(v.hayIzquierdo()) cola.mete(v.izquierdo);
        if(v.hayDerecho()) cola.mete(v.derecho);
      }
      return v;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
      if(this.elementos == 0) return -1;
      int  h = (int)Math.floor(Math.log(this.elementos)/Math.log(2));
      return h;
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
      Cola<Vertice> cola = new Cola<Vertice>();
      cola.mete(this.raiz);
      while(!cola.esVacia()){
        Vertice v = cola.saca();
        accion.actua(v);
        if(v.izquierdo != null) cola.mete(v.izquierdo);
        if(v.derecho != null) cola.mete(v.derecho);
      }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
