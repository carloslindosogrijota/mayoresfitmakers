package com.example.mayoresfitmakers.datos.repositorio

import android.util.Log
import com.example.mayoresfitmakers.modelo.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class PerfilUsuarioRepository {

    interface PerfilCallback {
        fun onPerfilOk(perfil: Usuario)
        fun onPerfilNoExiste()
        fun onPerfilError(mensaje: String)
    }

    private val firestore = FirebaseFirestore.getInstance()
    private val coleccionUsuarios = "usuarios"

    fun obtenerPerfil(uid: String, callback: PerfilCallback) {

        Log.d("FIRESTORE_DEBUG", "Entrando a obtenerPerfil uid=$uid")

        if (uid.isBlank()) {
            callback.onPerfilError("No se puede obtener perfil: uid vacío.")
            return
        }

        firestore.collection(coleccionUsuarios)
            .document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->

                Log.d(
                    "FIRESTORE_DEBUG",
                    "GET ok docId=${documentSnapshot.id} existe=${documentSnapshot.exists()}"
                )

                if (documentSnapshot.exists()) {

                    val datosCrudos = documentSnapshot.data

                    Log.d("FIRESTORE_DEBUG", "Claves Firestore=${datosCrudos?.keys}")
                    Log.d("FIRESTORE_DEBUG", "Contenido Firestore=$datosCrudos")

                    val perfil = documentSnapshot.toObject(Usuario::class.java)

                    if (perfil != null) {
                        perfil.id = documentSnapshot.id
                        Log.d("FIRESTORE_DEBUG", "toObject OK perfil=$perfil")
                        callback.onPerfilOk(perfil)
                    } else {
                        Log.e("FIRESTORE_DEBUG", "toObject devolvió null")
                        callback.onPerfilError("No se pudo leer el perfil (objeto nulo).")
                    }

                } else {
                    Log.d("FIRESTORE_DEBUG", "Documento NO existe")
                    callback.onPerfilNoExiste()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE_DEBUG", "GET fallo uid=$uid", exception)
                callback.onPerfilError("Error al obtener perfil: ${exception.message}")
            }
    }

    fun crearPerfil(perfil: Usuario, callback: PerfilCallback) {

        val idUsuario = perfil.id.trim()

        Log.d("FIRESTORE_DEBUG", "crearPerfil id=$idUsuario perfil=$perfil")

        if (idUsuario.isBlank()) {
            callback.onPerfilError("No se puede crear perfil: id vacío.")
            return
        }

        firestore.collection(coleccionUsuarios)
            .document(idUsuario)
            .set(perfil)
            .addOnSuccessListener {
                Log.d("FIRESTORE_DEBUG", "Perfil creado correctamente id=$idUsuario")
                callback.onPerfilOk(perfil)
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE_DEBUG", "Error al crear perfil", exception)
                callback.onPerfilError("Error al crear perfil: ${exception.message}")
            }
    }
}
