package com.example.mayoresfitmakers.modelo

import com.google.firebase.Timestamp

data class Evento(
    var id: String = "",

    var tipo: String = "",
    var lugar: String = "",

    var fecha: Long = 0L,
    var imagenUrl: String = "",

    var cupo_max: Int = 0,
    var inscripciones: Int = 0
)
