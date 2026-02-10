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
import com.example.mayoresfitmakers.datos.repositorio.MisActividadesRepository

class SenderismoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnApuntate: Button
    private lateinit var repository: MisActividadesRepository
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var snapHelper: PagerSnapHelper
    private lateinit var adapter: SenderismoAdapter

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
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadsenderismos() {
        repository = MisActividadesRepository() 
        
        repository.obtenerSenderismo(10, object : MisActividadesRepository.SenderismoCallback {
            override fun onOk(lista: List<Senderismo>) {
                senderismos.clear()
                senderismos.addAll(lista)
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onVacio() {
                senderismos.clear()
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onError(mensaje: String) {
                senderismos.clear()
                recyclerView.adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun setupRecyclerView() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = SenderismoAdapter(senderismos)
        snapHelper = PagerSnapHelper()

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position: Int = layoutManager.findFirstVisibleItemPosition()

                    if (position != currentPosition) {
                        currentPosition = position
                    }
                }
            }
        })
    }
}
