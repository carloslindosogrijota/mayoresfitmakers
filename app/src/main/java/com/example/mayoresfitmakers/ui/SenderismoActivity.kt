package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Senderismo
import com.example.proyectopruebas.SenderismoAdapter

/**
 * Activity simplificada que muestra historias en RecyclerView horizontal
 * Solo navegación manual, sin temporizador
 */
class SenderismoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnApuntate: Button

    private val senderismos = mutableListOf<Senderismo>()
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senderismo)



        initializeViews()
        loadsenderismos()
        setupRecyclerView()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.senderismosRecyclerView)
        btnApuntate = findViewById(R.id.btnApuntate)

        // Por ahora el botón no hace nada
        btnApuntate.setOnClickListener {
            // Implementar acción
        }
    }

    /**
     * Carga las historias de ejemplo
     */
    private fun loadsenderismos() {
        senderismos.clear()

        senderismos.add(Senderismo(nombre = "Ruta 1", imageResId = R.drawable.imagenprimeraruta))
        senderismos.add(Senderismo(nombre = "Ruta 2", imageResId = R.drawable.imagensegundaruta))
        senderismos.add(Senderismo(nombre = "Ruta 3", imageResId = R.drawable.imagenterceraruta))
        senderismos.add(Senderismo(nombre = "Ruta 4", imageResId = R.drawable.imagencuartaruta))
    }


    /**
     * Configura el RecyclerView horizontal
     */
    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = SenderismoAdapter(senderismos)

        // Snap para que se centre cada imagen
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Detectar cambios de posición
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (position != currentPosition) {
                        currentPosition = position
                    }
                }
            }
        }
        )
    }
}