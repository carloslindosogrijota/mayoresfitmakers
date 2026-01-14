package com.example.mayoresfitmakers.modelo.infraestructura

import com.example.mayoresfitmakers.modelo.Patologia

data class Usuario(
    val id: Long,
    val nombre: String,
    val correo: String,
    val edad: Int,
    val patologias: List<Patologia>,
)
