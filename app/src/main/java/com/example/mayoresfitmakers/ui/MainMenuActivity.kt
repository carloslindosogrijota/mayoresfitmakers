package com.example.mayoresfitmakers.ui.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.databinding.ActivityMainMenuBinding
import com.example.mayoresfitmakers.datos.repositorio.PerfilUsuarioRepository
import com.example.mayoresfitmakers.modelo.Usuario
import com.example.mayoresfitmakers.ui.recompensa.RecompensaActivity
import com.example.mayoresfitmakers.ui.actividades.ActividadesActivity
import com.example.mayoresfitmakers.ui.PerfilActivity
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var perfilRepository: PerfilUsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        perfilRepository = PerfilUsuarioRepository()

        configurarClicks()
        cargarNombreUsuario()
    }

    private fun configurarClicks() {
        binding.cardActividades.setOnClickListener {
            val intent = Intent(this, ActividadesActivity::class.java)
            startActivity(intent)
        }

        binding.cardRecompensa.setOnClickListener {
            val intent = Intent(this, RecompensaActivity::class.java)
            startActivity(intent)
        }

        binding.cardPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarNombreUsuario() {
        val uid = auth.currentUser?.uid

        if (uid.isNullOrBlank()) {
            binding.txtSaludo.text = "¡Hola!"
            return
        }

        perfilRepository.obtenerPerfil(uid, object : PerfilUsuarioRepository.PerfilCallback {

            override fun onPerfilOk(perfil: Usuario) {
                val nombreMostrado = perfil.nombre.trim()
                if (nombreMostrado.isNotEmpty()) {
                    binding.txtSaludo.text = "¡Hola, $nombreMostrado!"
                } else {
                    binding.txtSaludo.text = "¡Hola!"
                }
            }

            override fun onPerfilNoExiste() {
                binding.txtSaludo.text = "¡Hola!"
            }

            override fun onPerfilError(mensaje: String) {
                binding.txtSaludo.text = "¡Hola!"
            }
        })
    }
}
