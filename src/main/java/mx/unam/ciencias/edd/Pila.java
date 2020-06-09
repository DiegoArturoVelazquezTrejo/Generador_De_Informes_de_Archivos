package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        String m = "";
        Nodo nodo = cabeza;
        while(nodo != null){
            m += String.valueOf(nodo.elemento) + "\n";
            nodo = nodo.siguiente;
          }
        return m;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento invlaido");
        Nodo nodo = new Nodo(elemento);
        if(cabeza == null){
          nodo.siguiente = cabeza;
          cabeza = rabo = nodo;
        }else{
          nodo.siguiente = cabeza;
          cabeza = nodo;
        }
    }
}
