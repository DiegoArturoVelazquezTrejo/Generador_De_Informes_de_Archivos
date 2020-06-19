package mx.unam.ciencias.edd.proyecto3.graficas;
import mx.unam.ciencias.edd.*;
import mx.unam.ciencias.edd.proyecto3.*;
import java.io.IOException;
/**
* Clase para dibujar las gráficas de barras
* Dibujará una gráfica de barras en svg con base en los datos que se le pasen.
*/
public class GraficaBarras{
  /* Clase privada de punto para modelar los puntos en el svg */
  private class Punto{
    /* Coordenada en X*/
    double x;
    /* Coordenada en Y */
    double y;
    /* altura del cuadrado que se pintará con este punto */
    double height;
    /* Elemento que se graficará*/
    String dato;
    /* Porcentaje que ocupa sobre el total */
    double porcentaje;
    /* Constructor de la clase Punto */
    public Punto(double x, double y, String dato, double porcentaje, double height){
      this.x = x;
      this.y = y;
      this.height = height;
      this.dato = dato;
      this.porcentaje = porcentaje;
    }
    public String toString(){
      return dato + " coordenada x: "+x + " , coordenada y: "+y+" , altura: "+height;
    }

  }
  /* Lista con los puntos que se graficarán */
  private Lista<Punto> puntos;
  /* Coordenada en X donde iniciará la gŕafica  */
  private int inicioX = 20;
  /* Coordenada en Y donde iniciará la gŕafica  */
  private int inicioY = 350;
  /* Altura máxima de las barras  */
  private int alturaBarras = 1200;
  /* Longitud máxima de las barras */
  private int longitudBarras = 800;
  /* Longitud por cuadrado */
  private double longitudXcuadrado = 0;
  /* String con la representación de la gráfica en SVG*/
  private String cadena = "<svg width = 900 height = 550 >\n";

  /* Método que construye los puntos en la gráfica de acuerdo a los datos  */
  public GraficaBarras(Lista<Palabra> palabras, int totalApariciones){
    if(palabras.getLongitud() > 13){
      while(palabras.getLongitud() > 13) palabras.eliminaUltimo();
    }
    puntos = new Lista<>();
    double x_coordenada, altura, porcentaje;
    x_coordenada = inicioX;
    altura = porcentaje = 0;
    longitudXcuadrado = longitudBarras / palabras.getLongitud();
    int i = 0;
    int coordY = inicioY; 
    for(Palabra palabra : palabras){
      porcentaje = (palabra.getApariciones() * 100) / totalApariciones;
      altura = (palabra.getApariciones() * alturaBarras) / totalApariciones;
      if((i % 2) == 0)
	  coordY = inicioY;
      else
	  coordY = inicioY + 8;
      i++; 
      puntos.agrega(new Punto(x_coordenada, coordY, palabra.getPalabra(), porcentaje, altura));
      x_coordenada+= longitudXcuadrado;
    }
  }
  /**
  * Método que regresa la representación en svg de la gráfica de barras
  * @return String representación en svg de la gráfica de pastel
  */
  public String barras(){
    // Vamos a agregarle el título
    cadena+=dibujaEncabezado("Reporte de apariciones de palabras (Gráfica de Barras)");
    cadena+=dibujaCuadrado(inicioX, inicioY+40, alturaBarras, longitudBarras);
    String cuadrado, etiqueta;
    for(Punto punto : puntos){
      cuadrado= dibujaCuadrado(punto.x, punto.y, punto.height, longitudXcuadrado);
      etiqueta = dibujaEtiqueta(punto);
      cadena+=cuadrado+etiqueta;
    }
    cadena+="</svg>\n";
    return cadena;
  }
  /**
  * Método para dibujar un cículo
  * @return Círculo en Svg
  */
  public String dibujaCuadrado(double x1, double y1, double height, double width){
    String[] colores = {"#FF4F38", "#AF74E8", "#8CDEFF", "#74E875", "#FFE980", "#FFE580", "#B069EB", "#8CDEFF", "#FF5838"};
    String color = colores[(int)(Math.random()*colores.length)];
    double coorY = inicioY-height;
    if(width > longitudXcuadrado) color = "transparent";
    return "<rect x = '"+x1 +"' y='"+coorY +"'width='"+ width +"' height='"+ height+"' style='fill:"+ color +";stroke-width:1;stroke:black' />";
  }
  /**
  * Método para dibujar la etiqueta del dato
  * @param String dato
  * @param double porcentaje
  * @param double x1
  * @param double y1
  * @return representación en svg de la etiqueta del dato
  */

  public String dibujaEtiqueta(Punto punto){
    double coorX = punto.x + (longitudXcuadrado)/2;
    double coordY = inicioY-punto.height-10;
    double cY = punto.y + 10;
    String tamano;
    if(punto.dato.length() > 10)
	tamano = "10px";
    else
	tamano = "12px"; 
    String etiquetaPorcentaje = "<text x= '"+coorX+"' y= '"+coordY+"' text-anchor='middle' fill='white' font-size='12px' font-family='Fira Mono' dy='0.5em'>"+
    punto.porcentaje + "%"+"</text>\n";
    return etiquetaPorcentaje+"<text x= '"+coorX+"' y= '"+cY+"' text-anchor='middle' fill='white' font-size='"+tamano+"' font-family='Fira Mono' dy='0.5em'>"+
    punto.dato+"</text>\n";
  }
  /**
  * Método para dibujar encabezado
  * @param String títul o
  * @param String representación en svg del título
  */
  public String dibujaEncabezado(String tit){
    double coordY = inicioY - alturaBarras-20;
    double coordX = inicioX + longitudBarras / 2;
    return "<text x= '"+coordX+"' y= '"+coordY+"' text-anchor='middle' fill='black' font-size='15px' font-family='Fira Mono' dy='0.5em'>"+
    tit+"</text>\n";
  }
}
