package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Evento
import com.example.mayoresfitmakers.ui.adapter.EventosAdapter

class EventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnApuntate: Button

    private lateinit var adapter: EventosAdapter
    private val eventos: MutableList<Evento> = mutableListOf()

    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventos)

        initializeViews()
        loadEventos()
        setupRecyclerView()
        setupListeners()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.eventosRecyclerView)
        btnApuntate = findViewById(R.id.btnApuntate)
    }

    private fun loadEventos() {
        eventos.clear()

        eventos.add(
            Evento(
                id = "1",
                tipo = "Caminata guiada",
                lugar = "Parque Central",
                imageResId = R.drawable.imagencuartaruta
            )
        )
        eventos.add(
            Evento(
                id = "2",
                tipo = "Estiramientos",
                lugar = "Centro de mayores",
                imageResId = R.drawable.imagenprimeraruta
            )
        )
        eventos.add(
            Evento(
                id = "3",
                tipo = "Senderismo suave",
                lugar = "Ruta del lago",
                imageResId = R.drawable.imagensegundaruta
            )
        )
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = EventosAdapter(eventos) { evento ->
            Toast.makeText(this, "Evento: ${evento.tipo} - ${evento.lugar}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = layoutManager.findFirstVisibleItemPosition()
                    if (position != currentPosition && position >= 0) {
                        currentPosition = position
                    }
                }
            }
        })
    }

    private fun setupListeners() {
        btnApuntate.setOnClickListener {
            if (eventos.isEmpty()) {
                Toast.makeText(this, "No hay eventos disponibles", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val eventoSeleccionado = eventos[currentPosition]
            Toast.makeText(this, "Te apuntaste a: ${eventoSeleccionado.tipo}", Toast.LENGTH_SHORT).show()
        }
    }
}
