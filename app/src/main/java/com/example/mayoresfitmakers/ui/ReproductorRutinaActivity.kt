package com.example.mayoresfitmakers.ui

import android.content.Intent
import com.example.mayoresfitmakers.ui.adaptador.EjerciciosAdapter
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.EjercicioRepository
import kotlinx.coroutines.launch

class ReproductorRutinaActivity : AppCompatActivity() {

    private val repositorio = EjercicioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reproductor_rutina)

        // 1. Recuperar lo que eligió el usuario
        val tipo = intent.getStringExtra("TIPO") ?: "fuerza"
        val nivel = intent.getStringExtra("NIVEL") ?: "bajo"
        val tiempo = intent.getIntExtra("TIEMPO", 10)

        val txtTitulo = findViewById<TextView>(R.id.txtTituloRutina)
        txtTitulo.text = "Rutina de ${tipo.uppercase()} ($tiempo min)"

        // 2. Cargar datos de Firebase en segundo plano
        // Usamos lifecycleScope para corrutinas (necesitas la dependencia lifecycle-runtime-ktx)
        lifecycleScope.launch {
            try {
                // Llamamos a tu repositorio mágico
                val listaEjercicios = repositorio.obtenerRutinaPersonalizada(tipo, nivel, tiempo)

                if (listaEjercicios.isNotEmpty()) {
                    configurarCarrusel(listaEjercicios)
                } else {
                    Toast.makeText(this@ReproductorRutinaActivity, "No se encontraron ejercicios...", Toast.LENGTH_LONG).show()
                }

            } catch (e: Exception) {
                Toast.makeText(this@ReproductorRutinaActivity, "Error cargando rutina", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón salir
        findViewById<Button>(R.id.btnFinalizar).setOnClickListener {
            startActivity(Intent(this, ActividadesActivity::class.java))
            finish() // Cierra la actividad y vuelve atrás
        }
    }

    private fun configurarCarrusel(lista: List<com.example.mayoresfitmakers.modelo.Ejercicio>) {
        val viewPager = findViewById<ViewPager2>(R.id.viewPagerRutina)
        val txtContador = findViewById<TextView>(R.id.txtContadorPasos)
        val btnFinalizar = findViewById<Button>(R.id.btnFinalizar)

        // CAMBIO AQUÍ: Usamos tu nuevo EjerciciosAdapter
        val adapter = EjerciciosAdapter(lista)
        viewPager.adapter = adapter

        // Listener para actualizar el contador "Ejercicio 1 de 5"
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val total = lista.size
                val actual = position + 1
                txtContador.text = "Ejercicio $actual de $total"

                // Mostrar botón "Terminar" solo en el último ejercicio
                if (actual == total) {
                    btnFinalizar.visibility = android.view.View.VISIBLE
                } else {
                    btnFinalizar.visibility = android.view.View.GONE
                }
            }
        })
    }
}