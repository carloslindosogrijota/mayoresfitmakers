package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.Evento
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Date

class EventoRepository {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //Collection que se apunta a la base de dato
    private val collection = db.collection("evento")

    // Colección para relación N:M (usuario-evento)
    private val inscripcionesCollection = db.collection("inscripciones_eventos")

    fun addEvento(evento: Evento, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val id: String = collection.document().id

        val data: Map<String, Any> = mapOf(
            "tipo" to evento.tipo,
            "lugar" to evento.lugar,
            "fecha" to Timestamp(Date(evento.fecha)),
            "cupo_max" to evento.cupo_max,
            "inscripciones" to evento.inscripciones,
            "imagenUrl" to evento.imagenUrl
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
            "fecha" to Timestamp(Date(evento.fecha)),
            "cupo_max" to evento.cupo_max,
            "inscripciones" to evento.inscripciones,
            "imagenUrl" to evento.imagenUrl
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

                    // Timestamp -> millis (seguro)
                    val fechaTs: Timestamp? = doc.getTimestamp("fecha")
                    val fecha: Long = fechaTs?.toDate()?.time ?: 0L

                    val cupo_maxLong: Long = doc.getLong("cupo_max") ?: 0L
                    val cupo_maxInt: Int = cupo_maxLong.toInt()

                    val inscritosLong: Long = doc.getLong("inscripciones") ?: 0L
                    val inscritosInt: Int = inscritosLong.toInt()

                    val imagenUrl: String = doc.getString("imagenUrl") ?: ""

                    val evento = Evento(
                        id = doc.id,
                        tipo = tipo,
                        lugar = lugar,
                        fecha = fecha,
                        imagenUrl = imagenUrl,
                        cupo_max = cupo_maxInt,
                        inscripciones = inscritosInt
                    )

                    lista.add(evento)
                }
            }

            onData(lista)
        }
    }

    /**
     * Relación usuario-evento (N:M)
     * Crea el documento inscripciones/{eventoId}_{usuarioId}
     * e incrementa inscripciones si hay cupo.
     */
    fun apuntarseAEvento(
        eventoId: String,
        usuarioId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val relacionId: String = "${eventoId}_${usuarioId}"
        val eventoDoc = collection.document(eventoId)
        val inscripcionDoc = inscripcionesCollection.document(relacionId)

        db.runTransaction { tx ->

            val eventoSnap = tx.get(eventoDoc)
            if (!eventoSnap.exists()) {
                throw IllegalStateException("El evento no existe")
            }

            // Evitar duplicado
            val relacionSnap = tx.get(inscripcionDoc)
            if (relacionSnap.exists()) {
                throw IllegalStateException("Ya estás apuntado a este evento")
            }

            val cupo_max: Int = (eventoSnap.getLong("cupo_max") ?: 0L).toInt()
            val inscritos: Int = (eventoSnap.getLong("inscripciones") ?: 0L).toInt()

            if (cupo_max > 0 && inscritos >= cupo_max) {
                throw IllegalStateException("No quedan plazas disponibles")
            }

            // 1) Crear relación
            val data: Map<String, Any> = mapOf(
                "eventoId" to eventoId,
                "usuarioId" to usuarioId,
                "fecha_inscripcion" to Timestamp.now()
            )
            tx.set(inscripcionDoc, data)

            // 2) Incrementar contador
            tx.update(eventoDoc, "inscripciones", inscritos + 1)

            null
        }.addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    /**
     * Elimina la relación y decrementa inscripciones (si existe la inscripción).
     */
    fun desapuntarseDeEvento(
        eventoId: String,
        usuarioId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val relacionId: String = "${eventoId}_${usuarioId}"
        val eventoDoc = collection.document(eventoId)
        val inscripcionDoc = inscripcionesCollection.document(relacionId)

        db.runTransaction { tx ->

            val relacionSnap = tx.get(inscripcionDoc)
            if (!relacionSnap.exists()) {
                throw IllegalStateException("No estabas apuntado a este evento")
            }

            val eventoSnap = tx.get(eventoDoc)
            val inscritos: Int = (eventoSnap.getLong("inscripciones") ?: 0L).toInt()

            // 1) Borrar relación
            tx.delete(inscripcionDoc)

            // 2) Decrementar contador (sin bajar de 0)
            val nuevo = if (inscritos > 0) inscritos - 1 else 0
            tx.update(eventoDoc, "inscripciones", nuevo)

            null
        }.addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    /**
     * Consulta rápida: ¿está apuntado?
     */
    fun estaApuntado(
        eventoId: String,
        usuarioId: String,
        onResult: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val relacionId: String = "${eventoId}_${usuarioId}"
        inscripcionesCollection.document(relacionId).get()
            .addOnSuccessListener { doc -> onResult(doc.exists()) }
            .addOnFailureListener { ex -> onFailure(ex) }
    }
}
