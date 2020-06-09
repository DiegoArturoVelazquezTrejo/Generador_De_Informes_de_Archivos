package mx.unam.ciencias.edd.proyecto3.estructuras_svg;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import java.io.IOException;
/**
* Clase para dibujar las estructuras de datos con nodos
* A pesar que se puede imprimir la gráfica analizando únicamente los datos de entrada,
* vamos a hacer uso de una estructura gráfica, principalmente para poder usar el método de son vecinos y ver como éste funciona.
* Cabe aclarar que no es la manera más óptima, sin embargo pretendo utilizar distintos métodos que la clase gráfica me puede ofrecer.
*/
public class DibujaGrafica<T extends Comparable<T>>{
  /* Clase interna que nos permitirá representar cada vértice con una coordenada x, y y su respectivo elemento */
  private class Punto{
    /* Elemento del punto */
    public T elemento;
    /* Coordenada en x*/
    public double x;
    /* Coordenada en y*/
    public double y;
    /**
    * Constructor de la clase Punto
    * @param int coordenada en x
    * @param int coordenada en y
    * @param T elemento
    * @param Color color
    */
    public Punto(double x,double y, T elemento ){
      this.x = x;
      this.y = y;
      this.elemento = elemento;
    }
  }
  /* Lista con los elementos que se agregarán a la estructura de datos*/
  private Lista<T> elementos;
  /* Gráfica que almacenará los elementos */
  private Grafica<T> graf = new Grafica<>();
  /*Cadena que contendrá la representación en SVG del árbol */
  private String estructuraSVG = "";
  /* Coordenada en X del centro de la circunferencia */
  private double xInicial = 300;
  /* Coordenada en Y del centro de la circunferencia*/
  private double yInicial = 300;
  /* Radio de la circunferencia */
  private double radio = 200;
  /**
  * Constructor por omisión
  */
  public DibujaGrafica(){}
  /**
  * Constructor de la clase DibujaGrafica
  * @param Lista<T> datos
  */
  public DibujaGrafica(Lista<T> lista){
    for(T elemento : lista)
      try{
        graf.agrega(elemento);
      }catch(Exception e){
        System.out.println("No se pueden ingresar elementos repetidos");
      }
  }
  /**
  * Método que establece las dimensiones del svg en donde se presentará la estructura
  * @param int ancho
  * @param int alto
  */
  public void estableceDimensiones(int ancho,int alto){
      this.estructuraSVG+= "<svg width='"+ancho+"'  height= '" + alto+ "' >\n";
  }
  /**
  * Método que genera la cadena de los puntos
  * @param Lista<Puntos> lista con los puntos que se graficarán
  * @return String con los puntos representados en svg
  */
  public String dibujaPuntos(Lista<Punto> listaPuntos){
    String vertices = "";
    for(Punto p : listaPuntos)
      vertices+=dibujaVertice(p);
    return vertices;
  }
  /**
  * Método para dibujar cada Punto
  * @param Punto que se dibujará
  * @return String representación en svg del putno
  */
  public String dibujaVertice(Punto p){
      return dibujaNodo(p.x, p.y, p.elemento);
  }
  /**
  * Método que dibuja cada nodo de la gráfica
  * @param int coordenada en x
  * @param int coordenada en y
  * @param T elemento
  * @return Representación en svg del nodo
  */
  public String dibujaNodo(double x, double y, T elemento){
    double y1 = y+2;
    String colorletra = "red";
    String[] splt = elemento.toString().split("/");
    return "<circle cx= '"+x+"' cy= '"+y+"' r='25' stroke='black' fill='white'  />\n<text x= '"+x+"' y= '"+y1+
    "' text-anchor='middle' fill='"+ colorletra+"' font-size='15px' font-family='Arial' dy='.1em'>"+
    splt[splt.length - 1].trim()+"</text>\n";
  }
  /**
  * Método para agregar la representación en svg de los aristas
  * @param Lista<Puntos> puntos que se conectarán
  * @return String representación de los aristas en svg
  */
  public String dibujaAristas(Lista<Punto> listaPuntos){
    String aristas = "";
    for(Punto p : listaPuntos){
      for(Punto q : listaPuntos){
        if(graf.sonVecinos(p.elemento, q.elemento))
          aristas+=conectaVertices(p, q);
      }
    }
    return aristas;
  }
  /**
  * Método para conectar dos Puntos
  * @param Punto p1
  * @param Punto p2
  * @return String representación del arista
  */
  public String conectaVertices(Punto p1, Punto p2){
    return dibujaArista(p1.x,p1.y,p2.x,p2.y);
  }
  /**
  * Método para dibujar una arista de un vértice a su vértice padre
  * @param double coordenada1 en x
  * @param double coordenada1 en y
  * @param double coordenada2 en x
  * @param double coordenada2 en y
  * @return Representación en cadena del arista
  */
  public String dibujaArista(double x1, double y1, double x2, double y2){
    double y1N, y2N;
    y1N = (y1 < yInicial) ? y1+25 : y1 -25;
    y2N = (y2 < yInicial) ? y2+25 : y2 -25;
    return "<line x1='"+x1+"' y1='"+y1N+"' x2='"+x2+"' y2='" + y2N+"' style='stroke:white; stroke-width:1'></line>\n";
  }
  /**
  * Método que asigna las coordenadas en x, y a cada punto
  * @return lista puntos
  */
  public Lista<Punto> asignaCoordenadas(){
    Lista<Punto> listaPuntos = new Lista<>();
    double angulo_por_vertice = 360 / graf.getElementos();
    double angulo, x, y;
    angulo = 0;
    for(T elemento : graf){
      x = radio * Math.cos(Math.toRadians(angulo)) + xInicial;
      y = radio * Math.sin(Math.toRadians(angulo)) + yInicial;
      listaPuntos.agrega(new Punto(x, y, elemento));
      angulo+=angulo_por_vertice;
    }
    return listaPuntos;
  }
  /** Método que conecta a dos elementos de la gráfica
  * @param T elemento 1
  * @param T elemento 2
  **/
  public void conecta(T elemento1, T elemento2){
    try{
      graf.conecta(elemento1, elemento2);
    }catch(IllegalArgumentException e){}
  }
  /**
  * Método para agregar elementos a la gráfica
  * @param T elemento
  */
  public void agrega(T elemento){
    try{
      this.graf.agrega(elemento);
    }catch(Exception e){
      System.out.println("No se pueden ingresar elementos repetidos");
    }
  }
  /**
  * Método para dibujar la gráfica
  * @return String representación en svg de la gráfica
  */
  public String dibujaGrafica(){
    if(graf.getElementos() == 0) return "";
    estableceDimensiones(650, 650);
    Lista<Punto> listaPuntos = asignaCoordenadas();
    String svgPuntos = dibujaPuntos(listaPuntos);
    String svgAristas = dibujaAristas(listaPuntos);
    return estructuraSVG+svgPuntos+svgAristas+"</svg>";
  }

}
