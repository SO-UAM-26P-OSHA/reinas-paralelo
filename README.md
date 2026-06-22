# Problema de las N-Reinas - Solución Paralela

Este repositorio contiene la implementación en Java de una solución paralela para el clásico problema de las N-Reinas utilizando la estrategia de **backtracking** (búsqueda con retroceso) y concurrencia mediante la interfaz `Runnable`.

## Archivos del Proyecto
* `Tablero.java`: Define la estructura del tablero usando un arreglo unidimensional para optimizar memoria.
* `ReinaParalelo.java`: Contiene la implementación paralela utilizando múltiples hilos.

---

## Evidencia y Comparación de Rendimiento

Se realizaron pruebas incrementando el tamaño del tablero para comparar el tiempo de ejecución entre la versión secuencial y la versión paralela en el mismo equipo. 

| Tamaño del Tablero | Soluciones Totales | Tiempo Secuencial (s) | Tiempo Paralelo (s) |
| :---: | :---: | :---: | :---: |
| 13x13 | 73,712 | 0.9714 | 0.2623 |
| 14x14 | 365,596 | 5.4606 | 1.2573 |
| 15x15 | 2,279,184 | 34.8075 | 7.7100 |

**Conclusión:**
Como se observa en la tabla, la versión paralela reduce drásticamente el tiempo de ejecución. Para el tablero más grande (15x15), el algoritmo en paralelo logró resolver los más de 2 millones de combinaciones en aproximadamente 7.7 segundos, frente a los casi 35 segundos de la versión secuencial. Esto demuestra una mejora de rendimiento evidente (más de 4 veces más rápido), aprovechando al máximo los núcleos del procesador al dividir el espacio de búsqueda desde el primer renglón.

---

## Análisis y Preguntas de la Práctica

**1. ¿Qué parte del algoritmo se paralelizó?**
Se paralelizó la asignación y exploración inicial a partir del primer renglón del tablero.

**2. ¿Por qué se eligió repartir el primer renglón?**
Porque cada columna inicial del primer renglón genera un subproblema independiente. Esto permite que el trabajo se pueda dividir fácilmente sin que los hilos dependan de los resultados de los demás.

**3. ¿Cada hilo comparte la misma lista de tableros o tiene su propia lista?**
Cada hilo tiene su propia lista local (instanciada en el método `calcularDesde`) y no se usa una lista compartida, garantizando así la seguridad entre hilos.

**4. ¿Por qué se usa `join()`?**
Se utiliza para obligar al hilo principal (`main`) a detenerse y esperar a que todos los hilos trabajadores terminen su ejecución antes de intentar sumar sus resultados.

**5. ¿Qué pasaría si el programa principal no esperara a los hilos?**
El hilo principal continuaría ejecutando el código inmediatamente, intentaría sumar los resultados de hilos que aún no terminan de calcular (obteniendo 0), imprimiría una respuesta incorrecta y finalizaría el programa prematuramente.

**6. ¿Por qué no se necesita `synchronized` en esta versión?**
Porque el algoritmo divide el problema en subproblemas independientes y cada hilo utiliza su propia lista local en memoria. Al no haber recursos compartidos que se modifiquen simultáneamente, no existen condiciones de carrera.

**7. ¿El número de hilos debe ser siempre igual al número de procesadores? Explique.**
No obligatoriamente, pero es lo ideal para tareas intensivas de CPU. Lanzar muchos más hilos que el número de procesadores lógicos disponibles provoca que el sistema operativo desperdicie tiempo cambiando constantemente entre hilos (cambio de contexto) en lugar de dejarlos procesar, disminuyendo el rendimiento general.

**8. ¿La versión paralela siempre será más rápida que la secuencial? Explique.**
No. El sistema operativo requiere tiempo y recursos para crear, gestionar y destruir los hilos. Si el tablero a resolver es muy pequeño (ej. N=4 o N=5), la versión secuencial terminará casi instantáneamente, siendo más rápida que el tiempo que le toma al sistema preparar la ejecución paralela.
