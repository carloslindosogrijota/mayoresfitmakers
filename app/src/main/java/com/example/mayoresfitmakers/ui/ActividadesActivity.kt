package com.example.mayoresfitmakers.ui.actividades

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.databinding.ActivityMisActividadesBinding
import com.example.mayoresfitmakers.ui.EventosActivity
import com.example.mayoresfitmakers.ui.SenderismoActivity

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
            val intent = Intent(this, SenderismoActivity::class.java)
            startActivity(intent)
        }


        binding.cardEjerciciosSuaves.setOnClickListener {
        }

        binding.cardEstiramientos.setOnClickListener {
            val intent = Intent(this, EventosActivity::class.java)
            startActivity(intent)
        }
    }

}
