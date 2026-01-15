package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.Evento
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class EventoRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collection = db.collection("Eventos")

    fun addEvento(evento: Evento, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id: String = collection.document().id

        val data: Map<String, Any> = mapOf(
            "tipo" to evento.tipo,
            "lugar" to evento.lugar,
            "imagen" to evento.imageResId
        )

        collection.document(id).set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    fun updateEvento(evento: Evento, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id: String = evento.id
        if (id.isBlank()) {
            onFailure(IllegalArgumentException("El id del evento está vacío"))
            return
        }

        val data: Map<String, Any> = mapOf(
            "tipo" to evento.tipo,
            "lugar" to evento.lugar,
            "imagen" to evento.imageResId
        )

        collection.document(id).update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    fun deleteEvento(id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        collection.document(id).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    fun listenEventos(
        onData: (List<Evento>) -> Unit,
        onError: (Exception) -> Unit
    ): ListenerRegistration {
        return collection.addSnapshotListener { snapshot, ex ->
            if (ex != null) {
                onError(ex)
                return@addSnapshotListener
            }

            val lista: MutableList<Evento> = mutableListOf()

            if (snapshot != null) {
                for (doc in snapshot.documents) {
                    val tipo: String = doc.getString("tipo") ?: ""
                    val lugar: String = doc.getString("lugar") ?: ""

                    // ✅ Como guardas un Int, Firestore lo devuelve como Long/Number
                    val imagenLong: Long = doc.getLong("imagen") ?: 0L
                    val imagenInt: Int = imagenLong.toInt()

                    val evento = Evento(
                        id = doc.id,
                        tipo = tipo,
                        lugar = lugar,
                        imageResId = imagenInt
                    )

                    lista.add(evento)
                }
            }

            onData(lista)
        }
    }
}
