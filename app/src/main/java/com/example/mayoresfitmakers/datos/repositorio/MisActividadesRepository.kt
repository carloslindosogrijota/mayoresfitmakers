package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.Ejercicio
import com.example.mayoresfitmakers.modelo.Senderismo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MisActividadesRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Nombres EXACTOS según tu ER. Si tu equipo decide cambiarlos en Firestore,
    // los cambias aquí y no en 10 archivos.
    private val coleccionSenderismo: String = "SENDERISMO"
    private val coleccionEjercicios: String = "EJERCICIOS"

    interface SenderismoCallback {
        fun onOk(lista: List<Senderismo>)
        fun onVacio()
        fun onError(mensaje: String)
    }

    interface EjerciciosCallback {
        fun onOk(lista: List<Ejercicio>)
        fun onVacio()
        fun onError(mensaje: String)
    }

    fun obtenerSenderismo(limite: Long, callback: SenderismoCallback) {
        firestore.collection(coleccionSenderismo)
            .orderBy("ruta", Query.Direction.ASCENDING)
            .limit(limite)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot == null || snapshot.isEmpty) {
                    callback.onVacio()
                    return@addOnSuccessListener
                }

                val lista = mutableListOf<Senderismo>()
                for (doc in snapshot.documents) {
                    val senderismo = doc.toObject(Senderismo::class.java)
                    if (senderismo != null) {
                        senderismo.id = doc.id
                        lista.add(senderismo)
                    }
                }

                if (lista.isEmpty()) {
                    callback.onVacio()
                } else {
                    callback.onOk(lista)
                }
            }
            .addOnFailureListener { e ->
                callback.onError(e.message ?: "Error al obtener senderismo")
            }
    }

    fun obtenerEjerciciosPorTipo(tipo: String, limite: Long, callback: EjerciciosCallback) {
        firestore.collection(coleccionEjercicios)
            .whereEqualTo("tipo", tipo)
            .orderBy("duracion", Query.Direction.ASCENDING)
            .limit(limite)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot == null || snapshot.isEmpty) {
                    callback.onVacio()
                    return@addOnSuccessListener
                }

                val lista = mutableListOf<Ejercicio>()
                for (doc in snapshot.documents) {
                    val ejercicio = doc.toObject(Ejercicio::class.java)
                    if (ejercicio != null) {
                        ejercicio.id = doc.id
                        lista.add(ejercicio)
                    }
                }

                if (lista.isEmpty()) {
                    callback.onVacio()
                } else {
                    callback.onOk(lista)
                }
            }
            .addOnFailureListener { e ->
                callback.onError(e.message ?: "Error al obtener ejercicios")
            }
    }
}
