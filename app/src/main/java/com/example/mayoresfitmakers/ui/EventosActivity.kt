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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

class EventosActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnApuntate: Button

    private lateinit var adapter: EventosAdapter
    private val eventos: MutableList<Evento> = mutableListOf()

    private val repo = EventoRepository()
    private var listener: ListenerRegistration? = null

    private var currentPosition = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eventos)

        recyclerView = findViewById(R.id.eventosRecyclerView)
        btnApuntate = findViewById(R.id.btnApuntate)

        setupRecycler()
        setupButton()
    }

    override fun onStart() {
        super.onStart()
        listener = repo.listenEventos(
            onData = { lista ->
                eventos.clear()
                eventos.addAll(lista)
                adapter.updateList(eventos)
                if (currentPosition >= eventos.size) currentPosition = 0
            },
            onError = {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onStop() {
        super.onStop()
        listener?.remove()
    }

    private fun setupRecycler() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.layoutManager = layoutManager
        adapter = EventosAdapter(eventos) {}
        recyclerView.adapter = adapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    currentPosition = layoutManager.findFirstVisibleItemPosition()
                }
            }
        })
    }

    private fun setupButton() {
        btnApuntate.setOnClickListener {

            val uid = FirebaseAuth.getInstance().currentUser?.uid
            if (uid.isNullOrBlank()) {
                Toast.makeText(this, "Debes iniciar sesión", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (eventos.isEmpty()) return@setOnClickListener

            val evento = eventos[currentPosition]

            repo.apuntarseAEvento(
                eventoId = evento.id,
                usuarioId = uid,
                onSuccess = {
                    Toast.makeText(this, "Apuntado a ${evento.tipo}", Toast.LENGTH_SHORT).show()
                },
                onFailure = {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

