package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.AutenticacionRepository
import com.example.mayoresfitmakers.datos.repositorio.PerfilUsuarioRepository
import com.example.mayoresfitmakers.modelo.infraestructura.Usuario


class LoginActivity : AppCompatActivity() {

    private val authRepositorio = AutenticacionRepository()
    private val perfilRepositorio = PerfilUsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // aquí leerías email/password y llamarías a intentarLogin()
    }

    private fun intentarLogin(correo: String, contrasena: String) {


        authRepositorio.login(correo, contrasena, object : AutenticacionRepository.LoginCallback {

            override fun onLoginOk(uid: String, email: String) {
                cargarPerfil(uid, correo)
            }

            override fun onLoginError(mensaje: String) {
                Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarPerfil(uid: String, correo: String) {

        perfilRepositorio.obtenerPerfil(uid, object : PerfilUsuarioRepository.PerfilCallback {

            override fun onPerfilOk(perfil: Usuario) {
                navegarMenu()
            }

            override fun onPerfilNoExiste() {
                val perfil = Usuario(
                    id = id,
                    nombre = "Sin nombre",
                    correo = correo
                )

                perfilRepositorio.crearPerfil(perfil, this)
            }

            override fun onPerfilError(mensaje: String) {
                Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navegarMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
