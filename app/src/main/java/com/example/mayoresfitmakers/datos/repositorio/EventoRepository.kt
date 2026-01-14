//package com.example.mayoresfitmakers.datos.repositorio
//
//import android.util.Log
//import com.example.mayoresfitmakers.modelo.Evento
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ListenerRegistration
//
//class EventoRepository {
//
//    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val collection = db.collection("Eventos")
//
//    // CREATE
//    fun addEvento(evento: Evento, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        val data: Map<String, Any> = mapOf(
//            "tipo" to evento.tipo,
//            "lugar" to evento.lugar
//        )
//
//        collection.add(data)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { ex -> onFailure(ex) }
//    }
//
//    // READ (Realtime listener)
//    fun listenEventos(
//        onChange: (List<Evento>) -> Unit,
//        onError: (Exception) -> Unit
//    ): ListenerRegistration {
//        return collection.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                Log.e("EventoRepository", "Error escuchando 'Eventos': ${error.message}", error)
//                onError(error)
//                return@addSnapshotListener
//            }
//
//            val lista: List<Evento> = snapshot?.documents?.mapNotNull { doc ->
//                val evento: Evento? = doc.toObject(Evento::class.java)
//                evento?.apply { id = doc.id }
//            } ?: emptyList()
//
//            onChange(lista)
//        }
//    }
//
//    // UPDATE (sobrescribe campos tipo/lugar)
//    fun updateEvento(evento: Evento, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        val id: String = evento.id ?: run {
//            onFailure(IllegalArgumentException("Evento.id es null (no se puede actualizar)"))
//            return
//        }
//
//        val data: Map<String, Any> = mapOf(
//            "tipo" to evento.tipo,
//            "lugar" to evento.lugar
//        )
//
//        collection.document(id).set(data)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { ex -> onFailure(ex) }
//    }
//
//    // DELETE
//    fun deleteEvento(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
//        collection.document(id).delete()
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { ex -> onFailure(ex) }
//    }
//}
