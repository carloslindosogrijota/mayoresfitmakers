package com.example.mayoresfitmakers.modelo.infraestructura

data class Usuario(
    val id: Long,
    val nombre: String,
    val correo: String,
    val edad: Int,
    val patologias: List<Patologia>,
)
