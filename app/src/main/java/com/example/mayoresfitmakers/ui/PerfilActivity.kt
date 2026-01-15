package com.example.mayoresfitmakers.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.example.mayoresfitmakers.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mayoresfitmakers.modelo.Patologia

class PerfilActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var txtNombre: TextView
    private lateinit var txtEdad: TextView
    private lateinit var txtRacha: TextView
    private lateinit var txtPatologias: TextView

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

        // Obtener datos del usuario
        cargarDatosUsuario()
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
                                val id = map["id"]?.toString()
                                val afecion = map["tipo"]?.toString() ?: ""
                                val descripcion = map["lugar"]?.toString() ?: ""

                                Patologia(
                                    id = id,
                                    afecion = afecion,
                                    descripcion = descripcion
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
                                "• ${it.afecion} - ${it.descripcion}"
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