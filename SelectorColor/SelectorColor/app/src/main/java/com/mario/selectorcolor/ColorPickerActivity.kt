package com.mario.selectorcolor

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ColorPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        findViewById<Button>(R.id.btnRojo).setOnClickListener {
            devolverColor("#F44336", getString(R.string.rojo))
        }
        findViewById<Button>(R.id.btnVerde).setOnClickListener {
            devolverColor("#4CAF50", getString(R.string.verde))
        }
        findViewById<Button>(R.id.btnAzul).setOnClickListener {
            devolverColor("#2196F3", getString(R.string.azul))
        }
    }

    private fun devolverColor(hex: String, nombre: String) {
        val intent = Intent()
        intent.putExtra(EXTRA_COLOR_HEX, hex)
        intent.putExtra(EXTRA_COLOR_NAME, nombre)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    companion object {
        const val EXTRA_COLOR_HEX = "COLOR_HEX"
        const val EXTRA_COLOR_NAME = "COLOR_NAME"
    }
}
