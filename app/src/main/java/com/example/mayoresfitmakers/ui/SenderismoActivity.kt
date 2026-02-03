package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Senderismo
import com.example.mayoresfitmakers.ui.adaptador.SenderismoAdapter

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


        btnApuntate.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)

        }
    }

    /**
     * Carga las rutas de senderismo desde Firebase Firestore
     */
    private fun loadsenderismos() {
        val repository = com.example.mayoresfitmakers.datos.repositorio.MisActividadesRepository()
        
        repository.obtenerSenderismo(10, object : com.example.mayoresfitmakers.datos.repositorio.MisActividadesRepository.SenderismoCallback {
            override fun onOk(lista: List<Senderismo>) {
                senderismos.clear()
                senderismos.addAll(lista)
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onVacio() {
                // No hay rutas disponibles
                senderismos.clear()
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onError(mensaje: String) {
                // Error al cargar datos
                senderismos.clear()
                recyclerView.adapter?.notifyDataSetChanged()
            }
        })
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