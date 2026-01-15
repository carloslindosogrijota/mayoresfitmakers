package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R

class SeleccionEjercicioActivity : AppCompatActivity() {

    // Variables para guardar las respuestas del usuario
    private var seleccionTipo: String = ""
    private var seleccionTiempo: Int = 10
    private var seleccionNivel: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_ejercicio)

        // Referencias a los Layouts (Contenedores)
        val paso1 = findViewById<LinearLayout>(R.id.layoutPaso1Tipo)
        val paso2 = findViewById<LinearLayout>(R.id.layoutPaso2Tiempo)
        val paso3 = findViewById<LinearLayout>(R.id.layoutPaso3Nivel)

        // --- PASO 1: TIPO ---
        findViewById<Button>(R.id.btnFuerza).setOnClickListener { avanzarPaso1("fuerza", paso1, paso2) }
        findViewById<Button>(R.id.btnAerobico).setOnClickListener { avanzarPaso1("aerobico", paso1, paso2) }
        findViewById<Button>(R.id.btnEstirar).setOnClickListener { avanzarPaso1("estirar", paso1, paso2) }

        // --- PASO 2: TIEMPO ---
        findViewById<Button>(R.id.btn10min).setOnClickListener { avanzarPaso2(10, paso2, paso3) }
        findViewById<Button>(R.id.btn20min).setOnClickListener { avanzarPaso2(20, paso2, paso3) }
        findViewById<Button>(R.id.btn30min).setOnClickListener { avanzarPaso2(30, paso2, paso3) }

        // --- PASO 3: NIVEL Y FIN ---
        findViewById<Button>(R.id.btnBajo).setOnClickListener { finalizarSeleccion("bajo") }
        findViewById<Button>(R.id.btnMedio).setOnClickListener { finalizarSeleccion("medio") }
        findViewById<Button>(R.id.btnAlto).setOnClickListener { finalizarSeleccion("avanzado") }
    }

    private fun avanzarPaso1(tipo: String, actual: View, siguiente: View) {
        seleccionTipo = tipo
        actual.visibility = View.GONE     // Oculta pantalla actual
        siguiente.visibility = View.VISIBLE // Muestra la siguiente
    }

    private fun avanzarPaso2(tiempo: Int, actual: View, siguiente: View) {
        seleccionTiempo = tiempo
        actual.visibility = View.GONE
        siguiente.visibility = View.VISIBLE
    }

    private fun finalizarSeleccion(nivel: String) {
        seleccionNivel = nivel

        // ¡AQUÍ ES DONDE PASAMOS A TU PARTE DEL RECYCLER VIEW!
        val intent = Intent(this, ReproductorRutinaActivity::class.java)
        intent.putExtra("TIPO", seleccionTipo)
        intent.putExtra("TIEMPO", seleccionTiempo)
        intent.putExtra("NIVEL", seleccionNivel)
        startActivity(intent)
    }
}