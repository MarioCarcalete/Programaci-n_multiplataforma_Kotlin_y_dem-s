package com.mario.calculadoras

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class BisiestoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bisiesto)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etAnio = findViewById<EditText>(R.id.etAnio)
        val btnComprobar = findViewById<Button>(R.id.btnComprobar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnComprobar.setOnClickListener {
            val anio = etAnio.text.toString().toIntOrNull()

            if (anio == null || anio <= 0) {
                etAnio.error = getString(R.string.error_anio)
                tvResultado.text = ""
                return@setOnClickListener
            }

            if (esBisiesto(anio)) {
                tvResultado.text = getString(R.string.es_bisiesto, anio)
                tvResultado.setTextColor(ContextCompat.getColor(this, R.color.si))
            } else {
                tvResultado.text = getString(R.string.no_bisiesto, anio)
                tvResultado.setTextColor(ContextCompat.getColor(this, R.color.no))
            }
        }
    }

    // Un año es bisiesto si es divisible entre 4 y no entre 100,
    // o si es divisible entre 400.
    private fun esBisiesto(anio: Int): Boolean {
        return (anio % 4 == 0 && anio % 100 != 0) || anio % 400 == 0
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
