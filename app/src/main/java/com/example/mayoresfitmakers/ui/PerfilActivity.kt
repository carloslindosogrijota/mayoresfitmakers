package com.example.mayoresfitmakers.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Patologia
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var txtNombre: TextView
    private lateinit var txtEdad: TextView
    private lateinit var txtRacha: TextView
    private lateinit var txtPatologias: TextView

    private lateinit var btnPerfil: LinearLayout
    private lateinit var btnMenu: LinearLayout
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Inicializar vistas
        txtNombre = findViewById(R.id.txtNombre)
        txtEdad = findViewById(R.id.txtEdad)
        txtRacha = findViewById(R.id.txtRacha)
        txtPatologias = findViewById(R.id.txtPatologias)

        btnPerfil = findViewById(R.id.btnPerfil)
        btnMenu = findViewById(R.id.btnMenu)
        btnBack = findViewById(R.id.btnBack)

        // Configurar listeners de botones
        configurarBotones()

        // Obtener datos del usuario
        cargarDatosUsuario()
    }

    private fun configurarBotones() {
        btnPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun cargarDatosUsuario() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            db.collection("usuarios").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        // Obtener datos básicos
                        val nombre = document.getString("nombre") ?: "Usuario"
                        val edad = document.getLong("edad")?.toInt() ?: 0

                        // Obtener lista de patologías
                        val patologiasList = document.get("patologias") as? List<Map<String, Any>> ?: emptyList()
                        val patologias = patologiasList.mapNotNull { map ->
                            try {
                                Patologia(
                                    id = map["id"] as? String,
                                    afecion = map["afecion"] as? String ?: "",
                                    descripcion = map["descripcion"] as? String ?: ""
                                )
                            } catch (e: Exception) {
                                null
                            }
                        }


                        // Actualizar UI
                        txtNombre.text = nombre
                        txtEdad.text = "$edad años"

                        // Racha hardcodeada
                        txtRacha.text = "7"

                        // Mostrar patologías
                        if (patologias.isEmpty()) {
                            txtPatologias.text = "Ninguna registrada"
                        } else {
                            txtPatologias.text = patologias.joinToString("\n") {
                                "• ${it.afecion}: ${it.descripcion}"
                            }
                        }
                    } else {
                        Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}