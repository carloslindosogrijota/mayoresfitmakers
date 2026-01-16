package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.ui.base.DetectorCaidaState
import com.google.android.material.button.MaterialButton

class CheckCaidaActivity : AppCompatActivity() {

    private lateinit var btnOk: MaterialButton
    private lateinit var btnLlamar: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_caida_activity)

        DetectorCaidaState.setDialogoAbierto(true)

        btnOk = findViewById(R.id.btn_ok)
        btnLlamar = findViewById(R.id.btn_llamar)

        btnOk.setOnClickListener {
            finish()
        }

        btnLlamar.setOnClickListener {
            // MVP: sin funcionalidad real (solo feedback)
            Toast.makeText(this, "Función de emergencia no implementada (MVP).", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        DetectorCaidaState.setDialogoAbierto(false)
    }
}
