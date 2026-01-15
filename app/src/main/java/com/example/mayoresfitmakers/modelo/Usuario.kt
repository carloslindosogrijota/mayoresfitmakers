package com.example.mayoresfitmakers.modelo

data class Usuario(
    var id: String = "",
    var correo: String = "",
    var nombre: String = "",
    var apellido: String = "",
    var edad: Int = 0,
    var direccion: String = "",
    var telefono: String = "",
    var dni: String = "",
    var patologias: List<String> = emptyList()
)
