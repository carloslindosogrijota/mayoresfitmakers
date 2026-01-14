package com.example.mayoresfitmakers.modelo.infraestructura

import com.example.mayoresfitmakers.modelo.Patologia


data class Usuario(
    val id: String,            // uid de Firebase Authentication
    val correo: String,        // dato informativo / visual
    val nombre: String,
    val apellido: String,
    val edad: Int,
    val direccion: String,
    val telefono: String,
    val dni: String,
    val patologias: List<Patologia>

)

