package com.mario.lightsout

import android.content.Context
import android.content.res.ColorStateList
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var grid: GridLayout
    private lateinit var tvMovimientos: TextView
    private lateinit var tvTiempo: TextView
    private lateinit var spTamano: Spinner

    private lateinit var juego: LightsOutGame
    private val botones = mutableListOf<MaterialButton>()

    private var colorPrimario = 0
    private var colorSecundario = 0

    // Temporizador
    private val handler = Handler(Looper.getMainLooper())
    private var tiempoAcumulado = 0L      // ms ya contados
    private var inicioTramo = 0L          // cuándo empezó a contar
    private var corriendo = false
    private var terminada = false

    private val actualizarTiempo = object : Runnable {
        override fun run() {
            mostrarTiempo()
            handler.postDelayed(this, 500)
        }
    }

    private var tono: ToneGenerator? = null

    private val tamanos = intArrayOf(3, 4, 5)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grid = findViewById(R.id.gridTablero)
        tvMovimientos = findViewById(R.id.tvMovimientos)
        tvTiempo = findViewById(R.id.tvTiempo)
        spTamano = findViewById(R.id.spTamano)
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        val btnNueva = findViewById<Button>(R.id.btnNueva)

        colorPrimario = ContextCompat.getColor(this, R.color.primario)
        colorSecundario = ContextCompat.getColor(this, R.color.secundario)

        tono = try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 60)
        } catch (e: RuntimeException) {
            null
        }

        // Restaurar estado si venimos de un giro de pantalla
        val posTamano = savedInstanceState?.getInt(K_POS_TAMANO) ?: 0
        juego = LightsOutGame(tamanos[posTamano])
        if (savedInstanceState != null) {
            val estado = savedInstanceState.getBooleanArray(K_CASILLAS)
            if (estado != null) {
                juego.restaurar(estado, savedInstanceState.getInt(K_MOVIMIENTOS))
            }
            tiempoAcumulado = savedInstanceState.getLong(K_TIEMPO)
            terminada = savedInstanceState.getBoolean(K_TERMINADA)
            if (savedInstanceState.getBoolean(K_CORRIENDO)) arrancarTemporizador()
        }

        spTamano.setSelection(posTamano, false)
        crearTablero()

        spTamano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (tamanos[pos] != juego.tamano) {
                    juego = LightsOutGame(tamanos[pos])
                    pararTemporizador(reset = true)
                    terminada = false
                    crearTablero()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnReiniciar.setOnClickListener {
            juego.reiniciar()
            pararTemporizador(reset = true)
            terminada = false
            actualizarTablero()
        }

        btnNueva.setOnClickListener {
            juego.nuevaPartida()
            pararTemporizador(reset = true)
            terminada = false
            actualizarTablero()
        }
    }

    /** Crea los botones del tablero según el tamaño elegido. */
    private fun crearTablero() {
        grid.removeAllViews()
        botones.clear()
        grid.columnCount = juego.tamano
        grid.rowCount = juego.tamano

        val separacion = resources.getDimensionPixelSize(R.dimen.separacion_casilla)
        val radio = resources.getDimensionPixelSize(R.dimen.radio_casilla)

        for (fila in 0 until juego.tamano) {
            for (col in 0 until juego.tamano) {
                val boton = MaterialButton(this).apply {
                    insetTop = 0
                    insetBottom = 0
                    cornerRadius = radio
                    minWidth = 0
                    minHeight = 0
                    minimumWidth = 0
                    minimumHeight = 0
                    setOnClickListener { alPulsarCasilla(fila, col) }
                }

                // Cada casilla ocupa el mismo espacio gracias al peso 1
                val params = GridLayout.LayoutParams(
                    GridLayout.spec(fila, 1f),
                    GridLayout.spec(col, 1f)
                ).apply {
                    width = 0
                    height = 0
                    setMargins(separacion, separacion, separacion, separacion)
                }
                grid.addView(boton, params)
                botones.add(boton)
            }
        }
        actualizarTablero()
    }

    private fun alPulsarCasilla(fila: Int, col: Int) {
        if (terminada) return

        if (!corriendo) arrancarTemporizador()

        juego.pulsar(fila, col)
        vibrar()
        tono?.startTone(ToneGenerator.TONE_PROP_BEEP, 60)
        actualizarTablero()

        // Comprobar victoria después de cada movimiento
        if (juego.hayVictoria()) {
            terminada = true
            pararTemporizador(reset = false)
            tono?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 400)
            mostrarVictoria()
        }
    }

    /** Pinta cada botón con su color según el estado de la matriz. */
    private fun actualizarTablero() {
        for (fila in 0 until juego.tamano) {
            for (col in 0 until juego.tamano) {
                val boton = botones[juego.indice(fila, col)]
                val encendida = juego.estaEncendida(fila, col)
                boton.backgroundTintList = ColorStateList.valueOf(
                    if (encendida) colorSecundario else colorPrimario
                )
                boton.contentDescription = getString(
                    R.string.casilla_desc, fila + 1, col + 1,
                    getString(if (encendida) R.string.estado_secundario else R.string.estado_primario)
                )
            }
        }
        tvMovimientos.text = getString(R.string.movimientos, juego.movimientos)
        mostrarTiempo()
    }

    private fun mostrarVictoria() {
        AlertDialog.Builder(this)
            .setTitle(R.string.victoria_titulo)
            .setMessage(getString(R.string.victoria_mensaje, juego.movimientos, formatear(tiempoTotal())))
            .setPositiveButton(R.string.aceptar, null)
            .show()
    }

    // ---------- Temporizador ----------

    private fun tiempoTotal(): Long =
        if (corriendo) tiempoAcumulado + (SystemClock.elapsedRealtime() - inicioTramo)
        else tiempoAcumulado

    private fun arrancarTemporizador() {
        inicioTramo = SystemClock.elapsedRealtime()
        corriendo = true
        handler.removeCallbacks(actualizarTiempo)
        handler.post(actualizarTiempo)
    }

    private fun pararTemporizador(reset: Boolean) {
        if (corriendo) tiempoAcumulado = tiempoTotal()
        corriendo = false
        handler.removeCallbacks(actualizarTiempo)
        if (reset) tiempoAcumulado = 0L
        mostrarTiempo()
    }

    private fun mostrarTiempo() {
        tvTiempo.text = getString(R.string.tiempo, formatear(tiempoTotal()))
    }

    private fun formatear(ms: Long): String {
        val segundos = ms / 1000
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60)
    }

    // ---------- Vibración ----------

    private fun vibrar() {
        val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if (vibrator == null || !vibrator.hasVibrator()) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(30)
        }
    }

    // ---------- Ciclo de vida ----------

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(K_POS_TAMANO, spTamano.selectedItemPosition)
        outState.putBooleanArray(K_CASILLAS, juego.casillas)
        outState.putInt(K_MOVIMIENTOS, juego.movimientos)
        outState.putLong(K_TIEMPO, tiempoTotal())
        outState.putBoolean(K_CORRIENDO, corriendo)
        outState.putBoolean(K_TERMINADA, terminada)
    }

    override fun onDestroy() {
        handler.removeCallbacks(actualizarTiempo)
        tono?.release()
        tono = null
        super.onDestroy()
    }

    companion object {
        private const val K_POS_TAMANO = "pos_tamano"
        private const val K_CASILLAS = "casillas"
        private const val K_MOVIMIENTOS = "movimientos"
        private const val K_TIEMPO = "tiempo"
        private const val K_CORRIENDO = "corriendo"
        private const val K_TERMINADA = "terminada"
    }
}
