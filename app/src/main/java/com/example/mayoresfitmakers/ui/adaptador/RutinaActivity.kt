package com.example.mayoresfitmakers.ui.adaptador

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.SenderismoActivity

class RutinaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rutina)

        val btnSenderismo = findViewById<Button>(R.id.btnSenderismo)
        val btnEjercicios = findViewById<Button>(R.id.btnEjercicios)

        btnSenderismo.setOnClickListener {
            val intent = Intent(this, SenderismoActivity::class.java)
            startActivity(intent)
        }

        /**btnEjercicios.setOnClickListener {
            val intent = Intent(this, EjerciciosActivity::class.java)
            startActivity(intent)
        }*/
    }
}