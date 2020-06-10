package mx.unam.ciencias.edd.proyecto3.estructuras_svg;
import mx.unam.ciencias.edd.*;
/**
* Clase que dibuja árboles utilizando herramientas de la clase
* DibujaElementosArbolBinario
* Implementaremos el método toSVG que será el método mediante el cual el arbol se imprima.
*/
public class DibujaArbol<T extends Comparable<T>>{
  /*Cadena que contendrá la representación en SVG del árbol */
  private String arbolSVG = "";
  /*Lista que contendrá los elementos del árbol binario */
  private Lista<T> elementos;
  /* Variable que indica si el arbol que se quiere imprimir es AVL */
  private boolean esArbolAVL = false;

  /**
  * Método que establece las dimensiones del svg en donde se presentará el árbol
  */
  public void estableceDimensiones(EstructuraDatos estructura){
    if(this.elementos.getLongitud() > 0 && estructura != EstructuraDatos.ARBOLORDENADO){
      int altura = (int)Math.floor(Math.log(elementos.getLongitud())/Math.log(2));
      this.arbolSVG+= "\n<svg width='"+(elementos.getLongitud()*50)+"'  height= '" + altura*80+ "' >\n";
    }else{
      this.arbolSVG+= "\n<svg width='"+(elementos.getLongitud()*50)+"'  height= '" + elementos.getLongitud()*50+ "' >\n";
    }
  }
  /**
  * Constructor de la clase DibujaArbol
  * @param Lista<Integer> lista de ele mentos
  */
  public DibujaArbol(Lista<T> l){
    this.elementos = l;
  }

  public String dibujaArbol(EstructuraDatos estructura){
    /* Establecemos dimensiones con base en el tipo de árbol */
    estableceDimensiones(estructura);
    /* En caso de no tener elementos, no se dibuja nada */
    if(elementos.getLongitud() == 0)
      return "";
    /* Checaremos de qué tipo de arbol se trata */
    switch(estructura){
      /* En caso de ser un arbol ordenado, se concatena su representación en svg */
      case ARBOLORDENADO:
        arbolSVG+=dibujaArbolOrdenadoCompleto(estructura);
        break;
      /* En caso de ser un arbol completo, se concatena su representación en svg */
      case ARBOLCOMPLETO:
        arbolSVG+=dibujaArbolOrdenadoCompleto(estructura);
        break;
      /* En caso de ser un arbol rojinegro, se concatena su representación en svg */
      case ARBOLROJINEGRO:
        arbolSVG+=dibujaARbolAVLRojinegro(estructura);
        break;
      /* En caso de ser un arbol avl, se concatena su representación en svg */
      case ARBOLAVL:
        esArbolAVL = true;
        arbolSVG+=dibujaARbolAVLRojinegro(estructura);
        break;
      case NINGUNO:
        arbolSVG="";
        break;
    }
    /* Concatenamos la etiqueta de cierre */
    return arbolSVG;
  }

  /**
  * Método que dibuja un arbol binario ordenado / completo
  */
  public  String dibujaArbolOrdenadoCompleto(EstructuraDatos estructura){
      ArbolBinario<T> arbol;
      if(estructura.equals(EstructuraDatos.ARBOLORDENADO))
        arbol = new ArbolBinarioOrdenado<>(this.elementos);
      else
        arbol = new ArbolBinarioCompleto<>(this.elementos);
      /* Vamos a trabajar con la raíz del arbol qeu creamos */
      VerticeArbolBinario raiz = arbol.raiz();

      /* Calculamos la cantidad de vértices que hay en el subarbol izquierdo */
      int verticesEnIzquierdo, verticesEnDerecho;
      verticesEnDerecho = verticesEnIzquierdo = 0;
      if(raiz.hayIzquierdo())
        verticesEnIzquierdo = DibujaElmArbol.cuentaVerticesSubarbol(raiz.izquierdo());
      /* Calculamos la cantidad de vértices que hay en el subarbol derecho */
      if(raiz.hayDerecho())
        verticesEnDerecho = DibujaElmArbol.cuentaVerticesSubarbol(raiz.derecho());

      /* Calculamos la coordenada en x de la raíz */
      int xNueva = verticesEnIzquierdo*40+40;
      /* Inicializamos la coordenada en y de la raíz */
      int yNueva = 30;
      /* Calculamos el límite inferior del margen del arbol */
      int limiteInferior = 0;
      /* Calculamos el límite supperior del margen del arbol */
      int limiteSuperior = xNueva + (verticesEnDerecho)*40;

      String izq , der;
      izq = der = "";
      if(raiz.hayIzquierdo())
        izq = dibujaArbol(xNueva, yNueva, raiz.izquierdo(), limiteInferior, xNueva);
      if(raiz.hayDerecho())
        der = dibujaArbol(xNueva, yNueva, raiz.derecho(), xNueva, limiteSuperior);

      return DibujaElmArbol.dibujaNodo(xNueva, yNueva, raiz, Color.NINGUNO)+ der + izq+"</svg>";
  }

  /**
  * Método que dibuja un arbol binario avl/rojinegro
  */
  public String dibujaARbolAVLRojinegro(EstructuraDatos estructura){
    ArbolBinario<T> arbol;
    if(estructura.equals(EstructuraDatos.ARBOLAVL))
      arbol = new ArbolAVL<>(this.elementos);
    else
      arbol = new ArbolRojinegro<>(this.elementos);
    /* Vamos a trabajar con la raíz del arbol qeu creamos */
    VerticeArbolBinario raiz = arbol.raiz();

    /* Calculamos la cantidad de vértices que hay en el subarbol izquierdo */
    int verticesEnIzquierdo, verticesEnDerecho;
    verticesEnDerecho = verticesEnIzquierdo = 0;
    if(raiz.hayIzquierdo())
      verticesEnIzquierdo = DibujaElmArbol.cuentaVerticesSubarbol(raiz.izquierdo());
    /* Calculamos la cantidad de vértices que hay en el subarbol derecho */
    if(raiz.hayDerecho())
      verticesEnDerecho = DibujaElmArbol.cuentaVerticesSubarbol(raiz.derecho());

    /* Calculamos la coordenada en x de la raíz */
    int xNueva = verticesEnIzquierdo*40+40;
    /* Inicializamos la coordenada en y de la raíz */
    int yNueva = 30;
    /* Calculamos el límite inferior del margen del arbol */
    int limiteInferior = 0;
    /* Calculamos el límite supperior del margen del arbol */
    int limiteSuperior = xNueva + (verticesEnDerecho)*40;
    /* Obtenemos las respectivas etiquetas de cada vértice en caso de ser avl */
    String etiqueta, balance_altura;
    etiqueta = balance_altura = "";
    /* EN caso de ser rojinegro necesitamos el color de la raíz que es negro */
    Color color = Color.NINGUNO;
    /* Si es arbol avl creamos la etiqueta del nodo, sino, cambiamos el color del nodo por NEGRO */
    if(estructura.equals(EstructuraDatos.ARBOLAVL)){
      balance_altura = raiz.toString().substring(raiz.toString().length()-4, raiz.toString().length());
      etiqueta = DibujaElmArbol.dibujaEtiqueta(xNueva+5, yNueva-12, balance_altura);
    }else
      color = Color.NEGRO;
    String izq , der;
    izq = der = "";
    if(raiz.hayIzquierdo())
      izq = dibujaArbol(xNueva, yNueva, raiz.izquierdo(), limiteInferior, xNueva);
    if(raiz.hayDerecho())
      der = dibujaArbol(xNueva, yNueva, raiz.derecho(), xNueva, limiteSuperior);

    return etiqueta + DibujaElmArbol.dibujaNodo(xNueva, yNueva, raiz, color)+ der + izq+"</svg>\n";
  }
  /**
  * Método para dibujar un árbol binario
  * @param int[] coordenadas en x
  * @param int coordY coordenada del nodo pasado, por lo que la nueva coordenada debe ser coordY+incremento
  * @param int mitad Posición en donde se encuentra la coordenada en X pasada (se usará para dibujar el arista)
  * @param VerticeArbolBinario vértice actual que se representará en svg (se tiene que ver si es o no hoja)
  * @param int i corresponde al límite inferior del arreglo
  * @param int j corresponde al límite superior del arreglo
  * i, j nos ayudarán a poder ir calculando las mitades de cada subarreglo para encontrar la coordenada en x para cada nodo
  */
  public String dibujaArbol(int coorXAnterior, int coordYAnterior, VerticeArbolBinario v, int inferior, int superior){
    /* Caso en donde el vértice no existe (Caso base 1) */
    if(v == null)
      return "";
    /* Recalculamos la nueva coordenada en X, Y */
    int xNueva = DibujaElmArbol. calculaMitad(inferior, superior);
    int coordYNueva = coordYAnterior + 40;
    /* Primero dibujamos el arista del vértice pasado al nuevo vértice */
    String arista;
    int coordXAux = (v.padre().hayIzquierdo() && v.padre().izquierdo() == v) ? coorXAnterior -16  : coorXAnterior + 16;
    arista = DibujaElmArbol.dibujaArista(coordXAux, coordYAnterior, xNueva, coordYNueva);

    /* Tenemos que meternos en el lío de los colores */
    Color color;
    if(v.toString().substring(0,1).equals("R"))
      color = Color.ROJO;
    else if(v.toString().substring(0,1).equals("N"))
      color = Color.NEGRO;
    else
      color = Color.NINGUNO;
    String etiqueta, balance_altura;
    etiqueta = balance_altura = "";
    if(esArbolAVL){
      /* Obtenemos la subcadena del nodo que representa su altura/balance */
      balance_altura = v.toString().substring(v.toString().length()-4, v.toString().length());
      /*  Creamos la etiqueta txt de svg */
      coordXAux = (v.padre().hayIzquierdo() && v.padre().izquierdo() == v) ? xNueva-12 : xNueva+12;
      etiqueta = DibujaElmArbol.dibujaEtiqueta(coordXAux, coordYNueva-12, balance_altura);
    }
    /* Caso base que es cuando el vértice es hoja (Caso base 2) */
    if(DibujaElmArbol.esHoja(v))
      return etiqueta + arista + DibujaElmArbol.dibujaNodo(xNueva,coordYNueva,v,color);
    /* Vamos a ver si tiene hijo izquierdo */
    String izq, der;
    izq = der = "";
    if(v.hayIzquierdo())
        izq = dibujaArbol(xNueva, coordYNueva, v.izquierdo(), inferior, xNueva);
    /* Vamos a ver si tiene hijo derecho */
    if(v.hayDerecho())
        der = dibujaArbol(xNueva, coordYNueva, v.derecho(), xNueva, superior);
    return  etiqueta + arista + DibujaElmArbol.dibujaNodo(xNueva,coordYNueva,v,color)+ izq + der;
  }
}
