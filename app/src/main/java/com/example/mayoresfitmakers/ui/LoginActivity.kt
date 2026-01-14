package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.AutenticacionRepository
import com.example.mayoresfitmakers.datos.repositorio.PerfilUsuarioRepository
import com.example.mayoresfitmakers.modelo.Patologia
import com.example.mayoresfitmakers.modelo.infraestructura.Usuario
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private val authRepositorio = AutenticacionRepository()
    private val perfilRepositorio = PerfilUsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val contrasena = findViewById<EditText>(R.id.editTextPassword)
        val correo = findViewById<EditText>(R.id.editTextEmail)

        intentarLogin(correo.text.toString(), contrasena.text.toString())
    }

    private fun intentarLogin(correo: String, contrasena: String) {

        authRepositorio.login(correo, contrasena, object : AutenticacionRepository.LoginCallback {

            override fun onLoginOk(uid: String, correo: String) {
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

                val idUsuario: Long
                val idParseado: Long? = uid.toLongOrNull()
                if (idParseado != null) {
                    idUsuario = idParseado
                } else {
                    idUsuario = uid.hashCode().toLong()
                }

                val perfil = Usuario(
                    id = idUsuario,
                    nombre = "Sin nombre",
                    correo = correo,
                    edad = 0,
                    patologias = emptyList<Patologia>()
                )

                perfilRepositorio.crearPerfil(perfil, this)
            }

            override fun onPerfilError(mensaje: String) {
                Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navegarMenu() {
        //startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
