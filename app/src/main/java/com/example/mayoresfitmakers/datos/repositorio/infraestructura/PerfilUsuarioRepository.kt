package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.infraestructura.Usuario
import com.google.firebase.firestore.FirebaseFirestore


class PerfilUsuarioRepository {

    interface PerfilCallback {
        fun onPerfilOk(perfil: Usuario)
        fun onPerfilNoExiste()
        fun onPerfilError(mensaje: String)
    }

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun obtenerPerfil(uid: String, callback: PerfilCallback) {

        firestore
            .collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->

                if (doc.exists()) {
                    val perfil = doc.toObject(Usuario::class.java)
                    if (perfil != null) {
                        callback.onPerfilOk(perfil)
                    } else {
                        callback.onPerfilError("No se pudo convertir el perfil")
                    }
                } else {
                    callback.onPerfilNoExiste()
                }
            }
            .addOnFailureListener {
                callback.onPerfilError(it.message ?: "Error leyendo perfil")
            }
    }

    fun crearPerfil(perfil: Usuario, callback: PerfilCallback) {

        firestore
            .collection("usuarios")
            .document(perfil.id.toString())
            .set(perfil)
            .addOnSuccessListener {
                callback.onPerfilOk(perfil)
            }
            .addOnFailureListener {
                callback.onPerfilError(it.message ?: "Error creando perfil")
            }
    }
}
