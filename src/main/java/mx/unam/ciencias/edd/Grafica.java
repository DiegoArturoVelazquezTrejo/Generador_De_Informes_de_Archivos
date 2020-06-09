package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
          iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            color = Color.NINGUNO;
            this.vecinos = new Lista<Vecino>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Método para cambiar la distancia de un vértice */
        public void setDistancia(double distancia){
          this.distancia = distancia;
        }
        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (distancia > vertice.distancia)
                return 1;
            else if (distancia < vertice.distancia)
                return -1;
            return 0;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }
        /* Regresa el color del vecino. */
        public double getPeso() {
            return peso;
        }
        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos();
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento es <code>null</code> o ya
     *         había sido agregado a la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null || contiene(elemento)) throw new IllegalArgumentException("Elemento inválido");
        Vertice vertice = new Vertice(elemento);
        vertices.agregaFinal(vertice);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        conecta(a, b, 1.0);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
      if(!contiene(a) || !contiene(b)) throw new NoSuchElementException("No se ha encontrado alguno de los elemenots");
      if(a == b || sonVecinos(a, b) || peso < 0) throw new IllegalArgumentException("Los elementos "+a.toString()+ " "+b.toString()+" ya están conectados");
      Vertice verticeA = getVertice(a);
      Vertice verticeB = getVertice(b);
      verticeA.vecinos.agregaFinal(new Vecino(verticeB, peso));
      verticeB.vecinos.agregaFinal(new Vecino(verticeA, peso));
      this.aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        if(a == null || b == null || !contiene(a) || !contiene(b)) throw new NoSuchElementException("No se contienen los elementos "+a.toString()+" o "+b.toString());
        if(!sonVecinos(a, b)) throw new IllegalArgumentException("Los elementos "+a.toString()+" "+b.toString()+" No son vecinos");
        Vertice verticeA = getVertice(a);
        Vertice verticeB = getVertice(b);
        // Eliminamos el vecino B en la lista de vecinos de A
        Vecino vecinoB = getVecino(verticeA.vecinos, verticeB);
        Vecino vecinoA = getVecino(verticeB.vecinos, verticeA);
        // Eliminamos los vecinos de las listas
        verticeA.vecinos.elimina(vecinoB);
        verticeB.vecinos.elimina(vecinoA);

        aristas--;
    }
    /**
    * Método para obtener el vecino que tiene al vértice vertice
    * @param Lista<Vecino>
    * @param Vertice
    * @return Vecino
    */
    private Vecino getVecino(Lista<Vecino> lista, Vertice vertice){
      for(Vecino vec : lista){
        if(vec.vecino == vertice) return vec;
      }
      return null;
    }
    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
        Vertice vertice = getVertice(elemento);
        return vertice != null;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
        Vertice vertice = getVertice(elemento);
        if(vertice == null) throw new NoSuchElementException("No se encontró el elemento"+elemento.toString());
        // En este punto sabemos que el elemento que queremos eliminar sí está contenido en la gráfica
        int aristasEliminados = 0;

        for(Vecino v : vertice.vecinos){
          for(Vecino vecinoDelVecino : v.vecino.vecinos){
            if(vecinoDelVecino.vecino == vertice){
              v.vecino.vecinos.elimina(vecinoDelVecino);
              aristasEliminados++;
            }
          }
        }
        vertices.elimina(vertice);
        this.aristas -= aristasEliminados;
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if(a == null || b == null) throw new IllegalArgumentException("Elemento inválido");
        if(!contiene(a) || !contiene(b)) throw new NoSuchElementException("No se ha encontrado uno de los elementos");
        Vertice va = getVertice(a);
        Vertice vb = getVertice(b);
        if(va == null || vb == null) throw new NoSuchElementException("Se ha producido un error en son Vecinos");
        for(Vecino v : va.vecinos){
          if(v.vecino == vb) return true;
        }
        return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if(!contiene(a) || !contiene(b)) throw new NoSuchElementException("Alguno de los elementos no está contenido en la gráfica");
        if(!sonVecinos(a, b)) throw new IllegalArgumentException("Los elementos no están conectados");
        Vertice vA = getVertice(a);
        Vertice vB = getVertice(b);
        for(Vecino ve : vA.vecinos){
          if(ve.vecino == vB)
            return ve.getPeso();
        }
        return -1;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        if(!contiene(a) || !contiene(b)) throw new NoSuchElementException("Alguno de los elementos no está contenido en la gráfica");
        if(!sonVecinos(a, b)) throw new IllegalArgumentException("Los elementos no están conectados");
        Vertice vA = getVertice(a);
        Vertice vB = getVertice(b);
        for(Vecino ve : vA.vecinos){
          if(ve.vecino == vB)
            ve.peso = peso;
        }
        for(Vecino ve : vB.vecinos){
          if(ve.vecino == vA)
            ve.peso = peso;
        }
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
        Vertice v = null;
        for(Vertice vertice : vertices){
            if(vertice.get().equals(elemento))
              v = vertice;
        }
        if(v == null) throw new NoSuchElementException("No se ha encontrado el vértice");
        return v;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if(vertice == null || (vertice.getClass() != Vertice.class && vertice.getClass() != Vecino.class))
          throw new IllegalArgumentException("Vértice inválido");
        if(vertice.getClass() == Vertice.class){
          Vertice v = (Vertice) vertice;
          v.color = color;
        }
        if(vertice.getClass() == Vecino.class){
          Vecino v = (Vecino) vertice;
          v.vecino.color = color;
        }
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(vertices.getLongitud() == 0 || vertices.getLongitud() == 1) return true;
        Cola<Vertice> estructura = new Cola<>();
        Vertice w = vertices.getPrimero();
        paraCadaVertice(v -> setColor(v, Color.ROJO));
        setColor(w, Color.NEGRO);
        estructura.mete(w);
        while(!estructura.esVacia()){
          Vertice aux = estructura.saca();
          for(Vecino v : aux.vecinos)
            if(v.getColor().equals(Color.ROJO)){
              setColor(v, Color.NEGRO);
              estructura.mete(v.vecino);
            }
        }
        for(Vertice v : vertices)
          if(!v.getColor().equals(Color.NEGRO))
            return false;
        paraCadaVertice(v -> setColor(v, Color.NINGUNO));
        return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vertice : vertices)
          accion.actua(vertice);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        if(elemento == null || !contiene(elemento)) throw new NoSuchElementException("El elemento "+elemento.toString()+" no está en la gráfica");
        auxiliarRecorrido(elemento, accion, new Cola<Vertice>());
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        if(elemento == null || !contiene(elemento)) throw new NoSuchElementException("El elemento "+elemento.toString()+" no está en la gráfica");
        auxiliarRecorrido(elemento, accion, new Pila<Vertice>());
    }
    /**
    * Método auxiliar para el recorrido
    * @param T elemento por el que se iniciará
    * @param AccionVerticeGrafica<T> acción que se realizará a los vértices
    * @param MeteSaca<T> estructura que se utilizará
    */
    private void auxiliarRecorrido(T elemento, AccionVerticeGrafica<T> accion, MeteSaca<Vertice> estructura){
      paraCadaVertice(v -> setColor(v, Color.ROJO));
      Vertice w = getVertice(elemento);
      setColor(w, Color.NEGRO);
      estructura.mete(w);
      while(!estructura.esVacia()){
        Vertice aux = estructura.saca();
        accion.actua(aux);
        for(Vecino v : aux.vecinos)
          if(v.getColor().equals(Color.ROJO)){
            setColor(v, Color.NEGRO);
            estructura.mete(v.vecino);
          }
      }
      paraCadaVertice(v -> setColor(v, Color.NINGUNO));
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        paraCadaVertice(v -> setColor(v, Color.ROJO));
        String s = "{";
        String e = "{";
        for(Vertice v : vertices){
           s += v.get() + ", ";
          for(Vecino vv : v.vecinos){
            if(vv.getColor() == Color.ROJO)
              e += "(" + v.get() + ", " + vv.get() + "), ";
            v.color = Color.NEGRO;
          }
        }
        paraCadaVertice(v -> setColor(v, Color.NINGUNO));
        return s + "}, " + e + "}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        if(grafica.getAristas() != aristas || grafica.vertices.getLongitud() != vertices.getLongitud()) return false;
        /* Checamos si ambas gráficas tienen los mismos vértices */
        for(Vertice v : vertices){
          if(!grafica.contiene(v.get())) return false;
        }
        /* Vemos si tienen los mismos vecinos */
        for(Vertice v : vertices){
          for(Vecino vg : v.vecinos){
            if(!grafica.sonVecinos(v.elemento, vg.get()))
                return false;
          }
        }
        return true;
    }
    /**
    * Método para obtener el vértice que contiene el elemento T
    * @param T elemento
    * @return VerticeGrafica<T> vertice que contiene el elemento T
    */
    public Vertice getVertice(T elemento){
      if(elemento == null) throw new IllegalArgumentException("Elemento inválido");
      for(Vertice vertice : vertices)
          if(vertice.get().equals(elemento)) return vertice;
      return null;
    }
    /* Método que colorea el número de componentes conexas de la gráfica */
    private void coloreaComponenteConexa(Vertice v){
        Cola<Vertice> q = new Cola<>();
        setColor(v, Color.NEGRO);
        q.mete(v);
        while(!q.esVacia()){
          v = q.saca();
          for(Vecino w : v.vecinos)
            if(w.getColor().equals(Color.ROJO)){
              setColor(w, Color.NEGRO);
              q.mete(w.vecino);
            }
        }
    }
    /* Método que calcula el número de componentes conexas de la gráfica */
    private int calculaComponentesConexas(){
      paraCadaVertice((v) -> setColor(v, Color.ROJO));
      boolean c;
      int n=0;
      do{
        c=false;
        for(Vertice v: vertices) {
          if(v.getColor() !=Color.ROJO)
            continue;
          c=true;
          coloreaComponenteConexa(v);
          n++;
        }
      }while(c);
      return n;
    }
    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origent, T destinot) {
        if( origent == null || destinot == null) throw new IllegalArgumentException("Elementos inválidos");
        Vertice vertice;
        Vertice origen  = getVertice(origent);
        Vertice destino = getVertice(destinot);
        // Tenemos que ver si el vértice origen es igual al vértice destino
        if(origen == destino){
            Lista<VerticeGrafica<T>> lista = new Lista<>();
            lista.agregaFinal(origen);
            return lista;
        }
        inicializaDistancias();
        origen.distancia = 0;
        Cola<Vertice> colaVertices  = new Cola<>();
        colaVertices.mete(origen);
        // Mientras la cola tenga elementos
        while(!colaVertices.esVacia()){
          // Sacamos el primer vértice de la cola
          vertice = colaVertices.saca();
          // Para cada vecino de ese vértice vamos a estudiar su distancia
          for(Vecino v : vertice.vecinos){
            // Si es infinito, significa que vamos a actualizar la distancia
            if(v.vecino.distancia == Double.POSITIVE_INFINITY  ){
              // Redefinimos la distancia
              v.vecino.distancia = vertice.distancia + 1;
              // Metemos el vecino a la cola
              colaVertices.mete(v.vecino);
            }
          }
        }
        // Vamos a reconstruir la trayectoria
        return reconstruyeTrayectoria(origen, destino, false);
    }
    /* Método para reconstruir la trayectoria de una gráfica */
    private Lista<VerticeGrafica<T>> reconstruyeTrayectoria(Vertice origen, Vertice destino, boolean esDijkstra){
      Vertice auxiliar = destino;
      Lista<VerticeGrafica<T>> trayectoria = new Lista<>();
      if(destino.distancia != Double.POSITIVE_INFINITY){
        while(auxiliar != origen){
          for(Vecino vec : auxiliar.vecinos){
            if(auxiliar.distancia - (esDijkstra ? vec.peso : 1) == vec.vecino.distancia){
              trayectoria.agregaInicio(auxiliar);
              auxiliar = vec.vecino;
              break;
            }
          }
          if(auxiliar == origen) trayectoria.agregaInicio(origen);
        }
      }
      return trayectoria;
    }

    /* Método para inicializar las distancias */
    private void inicializaDistancias(){
      // Inicializamos todas las distancias a infinito
      for(Vertice vertice : vertices){
        vertice.distancia = Double.POSITIVE_INFINITY;
      }
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origent, T destinot) {
      if( origent == null || destinot == null) throw new IllegalArgumentException("Elementos inválidos");
      Vertice vertice;
      Vertice origen  = getVertice(origent);
      Vertice destino = getVertice(destinot);
      // Tenemos que ver si el vértice origen es igual al vértice destino
      if(origen == destino){
          Lista<VerticeGrafica<T>> lista = new Lista<>();
          lista.agregaFinal(origen);
          return lista;
      }
      // Inicializaremos todas las distancias de los vértices a cero
      inicializaDistancias();
      origen.distancia = 0;
      /* Creamos un montículo con los vértices de la gráfica */
      MonticuloDijkstra<Vertice> monticuloVertices;
      int numeroVertices = vertices.getLongitud();
      if(aristas > (numeroVertices*(numeroVertices - 1)/2)+numeroVertices)
        monticuloVertices = new MonticuloArreglo<>(vertices);
      else
        monticuloVertices = new MonticuloMinimo<>(vertices);
      // Mientras el montículo de vértices  tenga elementos
      while(!monticuloVertices.esVacia()){
        // Eliminamos la raíz del montículo
        vertice = monticuloVertices.elimina();
        // Para cada vecino de ese vértice vamos a estudiar su distancia
        for(Vecino v : vertice.vecinos){
          // Si es infinito, significa que vamos a actualizar la distancia
          if(v.vecino.distancia > vertice.distancia + v.getPeso() ){
            // Redefinimos la distancia
            v.vecino.distancia = vertice.distancia + v.getPeso();
            // Reordenamos el montículo
            monticuloVertices.reordena(v.vecino);
          }
        }
      }
      // Vamos a reconstruir la trayectoria
      return reconstruyeTrayectoria(origen, destino, true);
    }
    /**
    * Algoritmo de Prim para hallar el árbol de peso mínimo dentro de una gráfica
    */
    public Grafica<T> arbolPesoMinimo(){
      /* Primero checamos los casos en donde la gráfica no ses válida para el algoritmo de Prim */
      if(aristas == 0 ) throw new NoSuchElementException("No se ha encontrado una gráfica");
      /* Primero checamos los casos en donde la gráfica no ses válida para el algoritmo de Prim */
      if(!esConexa())   throw new IllegalStateException("La gráfica no es CONEXA");
      /* Preparamos todos los vértices para estudiarlos */
      paraCadaVertice(v -> setColor(v, Color.ROJO));
      inicializaDistancias();
      vertices.getPrimero().distancia = 0;
      return algoritmoPrim();
    }
    /**
    * Auxiliar del algoritmo de prim
    */
    private Grafica<T> algoritmoPrim(){
      Vertice vertice = vertices.getPrimero();
      Grafica<T> subGrafica = new Grafica<T>();
      // Creamos un minHeap
      MonticuloMinimo<Vertice> monticuloVertices = new MonticuloMinimo<>(vertices);
      while(!monticuloVertices.esVacia()){
        /* Obtenemos el vértice con menor peso del montículo */
        vertice = monticuloVertices.elimina();
        /* Si el vértice tiene color rojo es que no ha sido agregado a la gráfica de peso mínimo */
        if(vertice.getColor() == Color.ROJO){
              subGrafica.agrega(vertice.get());
              setColor(vertice, Color.NEGRO);
        }
        /* Recorremos los vecinos del vértice para actualizar sus distancias en caso de ser necesario */
        for(Vecino vecino_del_vertice : vertice.vecinos){
          /* Si el vértice tiene un peso mayor, se actualiza */
          if(vecino_del_vertice.vecino.distancia > vecino_del_vertice.peso && vecino_del_vertice.vecino.color != Color.NEGRO){
            vecino_del_vertice.vecino.distancia = vecino_del_vertice.peso;
            /* Reordenamos el montículo sobre l actualización de la distancia del vértice */
            monticuloVertices.reordena(vecino_del_vertice.vecino);
          }
        }
        /* Ahora vamos a conectar el vértice en la gráfica de peso mínimo */
        for(Vecino vec : vertice.vecinos){
          if(vec.getColor() == Color.NEGRO ){
            subGrafica.conecta(vec.get(), vertice.get());
            if(vertice.vecinos.getLongitud() !=1) vec.vecino.distancia = Double.POSITIVE_INFINITY;
            break;
          }
        }
      }
      return subGrafica;
  }
}
