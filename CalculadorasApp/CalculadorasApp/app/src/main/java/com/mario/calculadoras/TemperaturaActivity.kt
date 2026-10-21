package com.mario.calculadoras

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class TemperaturaActivity : AppCompatActivity() {

    // Posiciones de las unidades en el array de strings
    private val celsius = 0
    private val fahrenheit = 1
    private val kelvin = 2

    private val simbolos = arrayOf("°C", "°F", "K")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperatura)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etTemperatura = findViewById<EditText>(R.id.etTemperatura)
        val spOrigen = findViewById<Spinner>(R.id.spOrigen)
        val spDestino = findViewById<Spinner>(R.id.spDestino)
        val btnConvertir = findViewById<Button>(R.id.btnConvertir)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        spDestino.setSelection(fahrenheit)

        btnConvertir.setOnClickListener {
            // Se admite coma o punto como separador decimal
            val texto = etTemperatura.text.toString().replace(',', '.')
            val valor = texto.toDoubleOrNull()

            if (valor == null) {
                etTemperatura.error = getString(R.string.error_temp)
                tvResultado.text = ""
                return@setOnClickListener
            }

            val origen = spOrigen.selectedItemPosition
            val destino = spDestino.selectedItemPosition

            // Paso todo a Celsius y desde Celsius a la unidad destino
            val enCelsius = aCelsius(valor, origen)

            if (enCelsius < -273.15) {
                etTemperatura.error = getString(R.string.error_kelvin)
                tvResultado.text = ""
                return@setOnClickListener
            }

            val resultado = desdeCelsius(enCelsius, destino)

            tvResultado.text = String.format(
                Locale.getDefault(), "%.2f %s = %.2f %s",
                valor, simbolos[origen], resultado, simbolos[destino]
            )
        }
    }

    private fun aCelsius(valor: Double, unidad: Int): Double {
        return when (unidad) {
            fahrenheit -> (valor - 32) * 5 / 9
            kelvin -> valor - 273.15
            else -> valor
        }
    }

    private fun desdeCelsius(c: Double, unidad: Int): Double {
        return when (unidad) {
            fahrenheit -> c * 9 / 5 + 32
            kelvin -> c + 273.15
            else -> c
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
