package com.example.mayoresfitmakers.ui

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.EventoRepository
import com.example.mayoresfitmakers.modelo.Evento
import com.example.mayoresfitmakers.ui.adapter.EventosAdapter
import com.google.firebase.firestore.ListenerRegistration

class EventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnApuntate: Button

    private lateinit var adapter: EventosAdapter
    private val eventos: MutableList<Evento> = mutableListOf()

    private val repository: EventoRepository = EventoRepository()
    private var listenerRegistration: ListenerRegistration? = null

    private var currentPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventos)

        initializeViews()
        setupRecyclerView()
        setupListeners()
    }

    override fun onStart() {
        super.onStart()

        listenerRegistration = repository.listenEventos(
            onData = { lista ->
                eventos.clear()
                eventos.addAll(lista)
                adapter.updateList(eventos)
            },
            onError = { ex ->
                Toast.makeText(this, "Error cargando eventos: ${ex.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onStop() {
        super.onStop()
        listenerRegistration?.remove()
        listenerRegistration = null
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.eventosRecyclerView)
        btnApuntate = findViewById(R.id.btnApuntate)
    }

    private fun setupRecyclerView() {
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = EventosAdapter(eventos) { evento ->
            Toast.makeText(this, "Evento: ${evento.tipo} - ${evento.lugar}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        val snapHelper: PagerSnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = layoutManager.findFirstVisibleItemPosition()
                    if (position != currentPosition) {
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

            val eventoSeleccionado: Evento = eventos[currentPosition]
            Toast.makeText(this, "Apuntarte a: ${eventoSeleccionado.tipo}", Toast.LENGTH_SHORT).show()

            // Aquí luego conectas tu lógica real (guardar inscripción, navegar, etc.)
        }
    }
}
