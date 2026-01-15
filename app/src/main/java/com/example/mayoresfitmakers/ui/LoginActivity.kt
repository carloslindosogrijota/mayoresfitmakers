package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.datos.repositorio.infraestructura.AutenticacionRepository
import com.example.mayoresfitmakers.datos.repositorio.PerfilUsuarioRepository
import com.example.mayoresfitmakers.modelo.Usuario
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    private val authRepositorio = AutenticacionRepository()
    private val perfilRepositorio = PerfilUsuarioRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("LOGIN", "LoginActivity.onCreate")

        val correoInput = findViewById<EditText>(R.id.editTextEmail)
        val contrasenaInput = findViewById<EditText>(R.id.editTextPassword)
        val botonLogin = findViewById<MaterialButton>(R.id.buttonLogin)

        botonLogin.setOnClickListener {
            val correo = correoInput.text.toString().trim()
            val contrasena = contrasenaInput.text.toString()

            Log.d("LOGIN", "Botón login pulsado correo=$correo")

            intentarLogin(correo, contrasena)
        }
    }

    private fun intentarLogin(correo: String, contrasena: String) {

        Log.d("LOGIN", "Intentando login correo=$correo")

        authRepositorio.login(
            correo,
            contrasena,
            object : AutenticacionRepository.LoginCallback {

                override fun onLoginOk(uid: String, correo: String) {
                    Log.d("LOGIN", "Login OK uid=$uid correo=$correo")
                    cargarPerfil(uid, correo)
                }

                override fun onLoginError(mensaje: String) {
                    Log.e("LOGIN", "Login ERROR: $mensaje")
                    Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun cargarPerfil(uid: String, correo: String) {

        Log.d("LOGIN", "Llamando a obtenerPerfil uid=$uid")

        perfilRepositorio.obtenerPerfil(
            uid,
            object : PerfilUsuarioRepository.PerfilCallback {

                override fun onPerfilOk(perfil: Usuario) {
                    Log.d("LOGIN", "Perfil EXISTE perfil=$perfil")
                    navegarMenu()
                }

                override fun onPerfilNoExiste() {
                    Log.d("LOGIN", "Perfil NO existe, creando perfil")

                    val perfilNuevo = Usuario(
                        id = uid,
                        correo = correo,
                        nombre = "",
                        apellido = "",
                        edad = 0,
                        direccion = "",
                        telefono = "",
                        dni = "",
                        patologias = emptyList()
                    )

                    Log.d("LOGIN", "Perfil nuevo=$perfilNuevo")

                    perfilRepositorio.crearPerfil(
                        perfilNuevo,
                        object : PerfilUsuarioRepository.PerfilCallback {

                            override fun onPerfilOk(perfil: Usuario) {
                                Log.d("LOGIN", "Perfil creado OK")
                                navegarMenu()
                            }

                            override fun onPerfilNoExiste() {
                                Log.e("LOGIN", "Error: onPerfilNoExiste tras crear perfil")
                                Toast.makeText(
                                    this@LoginActivity,
                                    "No se pudo crear el perfil",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            override fun onPerfilError(mensaje: String) {
                                Log.e("LOGIN", "Error al crear perfil: $mensaje")
                                Toast.makeText(
                                    this@LoginActivity,
                                    mensaje,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }

                override fun onPerfilError(mensaje: String) {
                    Log.e("LOGIN", "Error al obtener perfil: $mensaje")
                    Toast.makeText(this@LoginActivity, mensaje, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun navegarMenu() {
        Log.d("LOGIN", "Navegando a PortadaActivity")
        startActivity(Intent(this, ActividadesActivity::class.java))
        finish()
    }
}
