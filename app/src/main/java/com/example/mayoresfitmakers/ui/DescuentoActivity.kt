package com.example.mayoresfitmakers.ui.descuento

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.databinding.ActivityDescuentoBinding

class DescuentoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescuentoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescuentoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarUI()
        configurarListeners()
    }

    private fun configurarUI() {
        binding.txtTitulo.text = "¡Felicidades!"
        binding.txtMensaje.text = "Has conseguido un 10% de descuento.\nMuestra este QR en caja para aplicarlo."
    }

    private fun configurarListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
