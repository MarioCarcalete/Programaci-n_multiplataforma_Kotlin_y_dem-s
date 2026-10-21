package com.mario.calculadoras

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnBisiesto = findViewById<Button>(R.id.btnBisiesto)
        val btnTemperatura = findViewById<Button>(R.id.btnTemperatura)

        btnBisiesto.setOnClickListener {
            startActivity(Intent(this, BisiestoActivity::class.java))
        }

        btnTemperatura.setOnClickListener {
            startActivity(Intent(this, TemperaturaActivity::class.java))
        }
    }
}
