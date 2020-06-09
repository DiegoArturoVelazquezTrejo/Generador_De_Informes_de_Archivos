package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        if(llave.length % 4 != 0){
          // Necesitamos que la longitud sea múltiplo de cuatro
          byte[] nuevoArreglo = new byte[llave.length + (4 - (llave.length % 4))];
          for(int i = 0; i < nuevoArreglo.length; i++){
            if(i < llave.length) nuevoArreglo[i] = llave[i];
            else nuevoArreglo[i] = (byte)0;
          }
          llave = nuevoArreglo;
        }
        int r, n;
        r = n = 0;
        for(int i = 0; i < llave.length; i+=4){
          n = combina_big_endian(llave[i], llave[i+1], llave[i+2], llave[i+3]);
          // Realizamos la operación XOR
          r^= n;
        }
        return r;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        int a, b, c;
        int[] arr = new int[3];
        a = b =  0x9e3779b9;
        c = 0xffffffff;
        int i = 0;
        /* Esta variable nos dirá cuandos bytes del arreglo sobraron */
        int residuo = llave.length % 12;
        // Vamos a iterar el arreglo de bytes mientras hayan 12 para realizar las operaciones
        while(i < llave.length - residuo && llave.length >=12){
          a+= combina_little_endian(llave[i],llave[i+1],llave[i+2],llave[i+3]);
          b+= combina_little_endian(llave[i+4],llave[i+5],llave[i+6],llave[i+7]);
          c+= combina_little_endian(llave[i+8],llave[i+9],llave[i+10],llave[i+11]);
          arr = mezcla_bob_jenkins(a,b,c);
          a = arr[0]; b = arr[1]; c = arr[2];
          i+=12;
        }
        c += llave.length;
        /* Tenemos que ver los residuos del arreglo para intentar construir los números que podamos */
        switch(residuo){
         case 11: c+=((llave[i+10] & 0xFF)<<24);
         case 10: c+=((llave[i+9] & 0xFF)<<16);
         case 9 : c+=((llave[i+8] & 0xFF)<<8);
         case 8 : b+=((llave[i+7] & 0xFF)<<24);
         case 7 : b+=((llave[i+6] & 0xFF)<<16);
         case 6 : b+=((llave[i+5] & 0xFF)<<8);
         case 5 : b+=(llave[i+4] & 0xFF);
         case 4 : a+=((llave[i+3] & 0xFF)<<24);
         case 3 : a+=((llave[i+2] & 0xFF)<<16);
         case 2 : a+=((llave[i+1] & 0xFF)<<8);
         case 1 : a+=(llave[i] & 0xFF);
        }
        arr = mezcla_bob_jenkins(a,b,c);
        return arr[2];
    }
    /**
    * Método que se encarga de mezclar tres enteros
    * @param int a
    * @param int b
    * @param int c
    */
    private static int[] mezcla_bob_jenkins(int a, int b, int c){
      int[] num = new int[3];
      // Aquí comienza el primer bloque de operaciones
      a-=b;   a-=c;   a^=(c >>> 13);
      b-=c;   b-=a;   b^=(a << 8);
      c-=a;   c-=b;   c^=(b >>> 13);
      // Aquí comienza el segundo bloque de operaciones
      a-=b;   a-=c;   a^=(c >>> 12);
      b-=c;   b-=a;   b^=(a << 16);
      c-=a;   c-=b;   c^=(b >>> 5);
      // Aquí comienza el tercer bloque de operaciones
      a-=b;   a-=c;   a^=(c >>> 3);
      b-=c;   b-=a;   b^=(a << 10);
      c-=a;   c-=b;   c^=(b >>> 15);

      num[0] = a; num[1] = b; num[2] = c;
      return num;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
      int h = 5381;
      for(int i = 0; i < llave.length; i++){
        h = (h* 33) + (llave[i] & 0xFF);
      }
      return h;
    }
    /**
    * Función que combina cuatro bytes en un entero de 32 bits en el esquema big-endian
    * @param byte a
    * @param byte b
    * @param byte c
    * @param byte d
    * @return int de 32 bits
    */
    private static int combina_big_endian(byte a, byte b, byte c, byte d){
      return ((a & 0xFF) << 24) | ((b & 0xFF) << 16) | ((c & 0xFF) << 8) | ((d & 0xFF));
    }
    /**
    * Función que combina cuatro bytes en un entero de 32 bits en el esquema little-endian
    * @param byte a
    * @param byte b
    * @param byte c
    * @param byte d
    * @return int de 32 bits
    */
    private static int combina_little_endian(byte a, byte b, byte c, byte d){
      return ((a & 0xFF)) | ((b & 0xFF) << 8) | ((c & 0xFF) << 16) | ((d & 0xFF) << 24);
    }
// Buenas referencias:
// https://burtleburtle.net/bob/hash/doobs.html
}
