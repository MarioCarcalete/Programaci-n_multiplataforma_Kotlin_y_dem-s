package com.mario.lightsout

import kotlin.random.Random

/**
 * Lógica del juego, separada de la interfaz.
 *
 * El tablero es una matriz de tamano x tamano guardada en un array de una
 * dimensión. La casilla (fila, col) está en la posición fila * tamano + col.
 *   false = color primario (apagada)
 *   true  = color secundario (encendida)
 */
class LightsOutGame(val tamano: Int) {

    var casillas = BooleanArray(tamano * tamano)
        private set

    var movimientos = 0
        private set

    // Desplazamientos de la propia casilla y sus vecinas (sin diagonales)
    private val direcciones = arrayOf(
        intArrayOf(0, 0),   // la propia casilla
        intArrayOf(-1, 0),  // arriba
        intArrayOf(1, 0),   // abajo
        intArrayOf(0, -1),  // izquierda
        intArrayOf(0, 1)    // derecha
    )

    fun indice(fila: Int, col: Int) = fila * tamano + col

    fun estaEncendida(fila: Int, col: Int) = casillas[indice(fila, col)]

    /** Algoritmo de adyacencia: cambia la casilla pulsada y sus vecinas. */
    fun pulsar(fila: Int, col: Int) {
        alternar(fila, col)
        movimientos++
    }

    private fun alternar(fila: Int, col: Int) {
        for (d in direcciones) {
            val f = fila + d[0]
            val c = col + d[1]
            // Solo si la vecina existe (no se sale del tablero)
            if (f in 0 until tamano && c in 0 until tamano) {
                val i = indice(f, c)
                casillas[i] = !casillas[i]
            }
        }
    }

    /** Victoria cuando todas las casillas están en color secundario. */
    fun hayVictoria() = casillas.all { it }

    /** Vuelve al estado inicial: todas en color primario. */
    fun reiniciar() {
        casillas = BooleanArray(tamano * tamano)
        movimientos = 0
    }

    /**
     * Nueva partida resoluble: parte del estado inicial (que tiene solución)
     * y aplica N clics aleatorios. Como cada clic es reversible (pulsar dos
     * veces la misma casilla la deja igual), el tablero siempre tiene solución.
     */
    fun nuevaPartida(random: Random = Random.Default) {
        do {
            reiniciar()
            val n = tamano * tamano
            repeat(n) {
                alternar(random.nextInt(tamano), random.nextInt(tamano))
            }
        } while (hayVictoria() || casillas.none { it })
        movimientos = 0
    }

    /** Para restaurar el estado al girar la pantalla. */
    fun restaurar(estado: BooleanArray, movs: Int) {
        if (estado.size == casillas.size) {
            casillas = estado.copyOf()
            movimientos = movs
        }
    }
}
