package com.example.mayoresfitmakers.ui.actividades

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.databinding.ActivityMisActividadesBinding

class ActividadesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMisActividadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMisActividadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarListeners()
    }

    private fun configurarListeners() {

        binding.btnVolver.setOnClickListener {
            finish()
        }

        binding.cardCaminar.setOnClickListener {
            // Caminar = Senderismo
            // TODO: Intent a SenderismoActivity
        }

        binding.cardEjerciciosSuaves.setOnClickListener {
            // TODO: Intent a RutinaActivity con extra tipo = "suave"
        }

        binding.cardEstiramientos.setOnClickListener {
            // TODO: Intent a RutinaActivity con extra tipo = "estiramiento"
        }
    }

}
