package com.mario.selectorcolor

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private lateinit var layoutPrincipal: ConstraintLayout
    private lateinit var tvColorActual: TextView

    // Color elegido (null = predeterminado). Se guardan para no perderlos al girar.
    private var colorHex: String? = null
    private var colorNombre: String? = null

    // Se registra antes de que la Activity esté en STARTED (como propiedad de la clase)
    private val elegirColorLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { resultado ->
            if (resultado.resultCode == Activity.RESULT_OK) {
                val datos = resultado.data
                val hex = datos?.getStringExtra(ColorPickerActivity.EXTRA_COLOR_HEX)
                val nombre = datos?.getStringExtra(ColorPickerActivity.EXTRA_COLOR_NAME)
                if (hex != null && nombre != null) {
                    aplicarColor(hex, nombre)
                }
            }
            // Si vuelve con RESULT_CANCELED (botón atrás) no se hace nada:
            // la pantalla mantiene el color que tenía.
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutPrincipal = findViewById(R.id.layoutPrincipal)
        tvColorActual = findViewById(R.id.tvColorActual)
        val btnElegirColor = findViewById<Button>(R.id.btnElegirColor)

        tvColorActual.text = getString(R.string.color_actual, getString(R.string.predeterminado))

        // Recuperar el color si se ha girado la pantalla
        val hexGuardado = savedInstanceState?.getString(KEY_HEX)
        val nombreGuardado = savedInstanceState?.getString(KEY_NOMBRE)
        if (hexGuardado != null && nombreGuardado != null) {
            aplicarColor(hexGuardado, nombreGuardado)
        }

        btnElegirColor.setOnClickListener {
            val intent = Intent(this, ColorPickerActivity::class.java)
            elegirColorLauncher.launch(intent)
        }
    }

    private fun aplicarColor(hex: String, nombre: String) {
        try {
            layoutPrincipal.setBackgroundColor(Color.parseColor(hex))
            tvColorActual.text = getString(R.string.color_actual, nombre)
            colorHex = hex
            colorNombre = nombre
        } catch (e: IllegalArgumentException) {
            // Código hexadecimal no válido: se deja el color como estaba
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_HEX, colorHex)
        outState.putString(KEY_NOMBRE, colorNombre)
    }

    companion object {
        private const val KEY_HEX = "color_hex"
        private const val KEY_NOMBRE = "color_nombre"
    }
}
