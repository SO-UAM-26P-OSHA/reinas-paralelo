import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Clase ReinaParalelo que implementa la solucion al problema de las n-reinas 
 * utilizando backtracking y paralelismo con hilos (Runnable).
 */
public class ReinaParalelo {

    // Lista enlazada para almacenar los diferentes estados del tablero durante la busqueda de soluciones.
    private LinkedList<Tablero> L = new LinkedList<Tablero>();

    /**
     * Constructor por defecto de la clase Reina.
     */
    public ReinaParalelo() {
        // Inicializacion vacia.
    }

    /**
     * Constructor que inicializa la lista con un tablero inicial.
     * @param t Tablero inicial con el que se comienza la solucion.
     */
    public ReinaParalelo(Tablero t) {
        L.add(t);
    }

    /**
     * Verifica si una reina en la columna especificada seria comida por otra reina
     * ya colocada en el tablero.
     * @param col     Columna donde se intenta colocar la reina.
     * @param tablero El estado actual del tablero.
     * @return true si la reina seria comida, false en caso contrario.
     */
    boolean esComida(int col, Tablero tablero) {
        boolean comida = true; // Inicialmente asumimos que la reina seria comida.
        int i, j = 0;
        int reng = tablero.getRenglon(); // Obtener el renglon actual del tablero.
        i = reng - 1;
        int[] tab = tablero.getTablero(); // Obtener el arreglo que representa el tablero.

        // Verificar si hay una reina en la misma columna en filas anteriores.
        while ((i >= 0) && (tab[i] != col)) {
            i--;
        }

        if (i < 0) {
            // Verificar la diagonal izquierda hacia arriba.
            i = reng - 1;
            j = col - 1;
            while ((i >= 0) && (j >= 0) && (tab[i] != j)) {
                i--;
                j--;
            }

            if ((i < 0) || (j < 0)) {
                // Verificar la diagonal derecha hacia arriba.
                i = reng - 1;
                j = col + 1;
                while ((i >= 0) && (j < Tablero.TAM) && (tab[i] != j)) {
                    i--;
                    j++;
                }

                if ((i < 0) || (j >= Tablero.TAM)) {
                    comida = false; // Si no hay ninguna amenaza, la reina no seria comida.
                }
            }
        }

        return comida;
    }

    /**
     * Calcula el numero de soluciones posibles para el problema de las n-reinas.
     * (Versión secuencial original mantenida como referencia)
     */
    int calcularReinas() {
        int num_sol = 0; 
        Tablero elem;
        Tablero nuevo;
        int r;
        int[] tab;

        try {
            while ((elem = L.removeLast()) != null) {
                r = elem.getRenglon(); 
                tab = elem.getTablero(); 

                if (r < Tablero.TAM) {
                    for (int col = 0; col < Tablero.TAM; col++) {
                        if (!esComida(col, elem)) { 
                            tab[r] = col; 
                            nuevo = new Tablero(tab.clone(), r + 1); 
                            tab[r] = -1; 
                            L.add(nuevo); 
                        }
                    }
                } else {
                    num_sol += 1;
                }
            }
        } catch (NoSuchElementException e) {
        }

        return num_sol;
    }

   
    /**
     * Creamos una clase TrabajadorReinas, para poder paralelizar el trabajo
     */
    class TrabajadorReinas implements Runnable {
        private int idHilo;
        private int totalHilos;
        private int soluciones;

        public TrabajadorReinas(int idHilo, int totalHilos) {
            this.idHilo = idHilo;
            this.totalHilos = totalHilos;
            this.soluciones = 0;
        }

        public int getSoluciones() {
            return soluciones;
        }

        /**
         * REcibe  un tablero inicial y cada hilo resuelve una parte del problema
         */
        private int calcularDesde(Tablero inicial) {
            int num_sol = 0;
            LinkedList<Tablero> listaLocal = new LinkedList<Tablero>();
            listaLocal.add(inicial);
            
            Tablero elem;
            Tablero nuevo;
            int r;
            int[] tab;

            try {
                // Reutilizamos la lógica exacta del backtracking secuencial
                while ((elem = listaLocal.removeLast()) != null) {
                    r = elem.getRenglon();
                    tab = elem.getTablero();

                    if (r < Tablero.TAM) {
                        for (int col = 0; col < Tablero.TAM; col++) {
                            if (!esComida(col, elem)) { // Usa el esComida original
                                tab[r] = col;
                                nuevo = new Tablero(tab.clone(), r + 1);
                                tab[r] = -1;
                                listaLocal.add(nuevo);
                            }
                        }
                    } else {
                        num_sol += 1;
                    }
                }
            } catch (NoSuchElementException e) {
            }

            return num_sol;
        }

        /**
         * Implementar el método run() para repartir las columnas del primer renglon
         */
        @Override
        public void run() {
            int totalLocal = 0;
            for (int col = idHilo; col < Tablero.TAM; col += totalHilos) {
                int[] tab = new int[Tablero.TAM];
                for (int i = 0; i < Tablero.TAM; i++) {
                    tab[i] = -1;
                }
                tab[0] = col;
                Tablero inicial = new Tablero(tab, 1);
                
                totalLocal += calcularDesde(inicial);
            }
            soluciones = totalLocal;
        }
    }

    /**
     * Metodo principal adaptado a los hilos
     */
    public static void main(String[] args) {
        // Instancia principal de la clase para poder crear a los trabajadores
        ReinaParalelo reinas = new ReinaParalelo(); 
        
        // A partir de los procesadores se  crean hilos
        int procesadores = Runtime.getRuntime().availableProcessors();
        int numHilos = Math.min(procesadores, Tablero.TAM);
        
        Thread[] hilos = new Thread[numHilos];
        TrabajadorReinas[] trabajadores = new TrabajadorReinas[numHilos];

        System.out.println("Iniciando busqueda paralela para tablero " + Tablero.TAM + "x" + Tablero.TAM + " con " + numHilos + " hilos...");
        
        //Cronometro
        long inicio = System.nanoTime();

        // Lanzar los hilos
        for (int i = 0; i < numHilos; i++) {
            trabajadores[i] = reinas.new TrabajadorReinas(i, numHilos);
            hilos[i] = new Thread(trabajadores[i], "Hilo-Reinas-" + i);
            hilos[i].start();
        }

        int totalSoluciones = 0;

        // Esperar a que terminen todos los hilos
        for (int i = 0; i < numHilos; i++) {
            try {
                hilos[i].join();
                totalSoluciones += trabajadores[i].getSoluciones();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Finalizar cronómetro
        long fin = System.nanoTime();
        double tiempoSegundos = (fin - inicio) / 1_000_000_000.0;

        System.out.println("Tablero: " + Tablero.TAM);
        System.out.println("Soluciones totales: " + totalSoluciones);
        System.out.println("Tiempo: " + tiempoSegundos + " segundos");        
    }
}
