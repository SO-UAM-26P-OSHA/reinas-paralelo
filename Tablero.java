import java.util.Arrays;

/**
 * La clase Tablero representa el estado de un tablero de ajedrez para el problema de las n-reinas.
 */
public class Tablero {

    static int TAM = 15; // Tamaño por defecto del tablero (8x8).
    private int[] tablero; // Arreglo que representa las posiciones de las reinas en el tablero.
    private int renglon; // Indica el número de reinas colocadas (y el siguiente renglon a evaluar).

    /**
     * Constructor que inicializa el tablero con un tamaño especificado.
     * 
     * @param n Tamaño del tablero (n x n).
     */
    public Tablero(int n) {
        TAM = n; // Establece el tamaño del tablero.
        tablero = new int[TAM]; // Inicializa el arreglo del tablero.
        renglon = 0; // Inicializa el renglon actual a 0.
        // Llena el tablero con -1 para indicar posiciones vacías.
        for (int i = 0; i < TAM; i++)
            tablero[i] = -1;
    }

    /**
     * Constructor por defecto que inicializa un tablero de tamaño TAM.
     */
    public Tablero() {
        this(TAM); // Llama al constructor con el tamaño por defecto.
    }

    /**
     * Constructor que inicializa el tablero con un estado específico.
     * 
     * @param tablero Arreglo que representa un estado particular del tablero.
     * @param renglon Número de reinas colocadas (siguiente renglon a evaluar).
     */
    public Tablero(int[] tablero, int renglon) {
        TAM = tablero.length; // Establece el tamaño del tablero basado en la longitud del arreglo.
        this.tablero = tablero; // Inicializa el tablero con el estado dado.
        this.renglon = renglon; // Establece el renglon actual.
    }

    /**
     * Obtiene el arreglo que representa el tablero.
     * 
     * @return El arreglo que representa el estado actual del tablero.
     */
    public int[] getTablero() {
        return tablero;
    }

    /**
     * Establece un nuevo estado para el tablero.
     * 
     * @param tablero Arreglo que representa el nuevo estado del tablero.
     */
    public void setTablero(int[] tablero) {
        this.tablero = tablero;
    }

    /**
     * Obtiene el número de reinas colocadas actualmente en el tablero.
     * 
     * @return El número de reinas colocadas (siguiente renglon a evaluar).
     */
    public int getRenglon() {
        return renglon;
    }

    /**
     * Establece el número de reinas colocadas en el tablero.
     * 
     * @param renglon Número de reinas colocadas (siguiente renglon a evaluar).
     */
    public void setRenglon(int renglon) {
        this.renglon = renglon;
    }



    public void set_R (int pos, int num) {
        this.tablero[pos] = num;
        this.setRenglon(pos +1);
    }

    /**
     * Devuelve una representación en forma de cadena del estado actual del tablero.
     * 
     * @return Una cadena que representa el arreglo del tablero.
     */
    @Override
    public String toString() {
        return Arrays.toString(tablero);
    }

}

