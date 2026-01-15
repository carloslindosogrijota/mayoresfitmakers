package com.example.mayoresfitmakers.modelo

data class Ejercicio(
    var id: String = "",
    var nombre: String = "",
    var tipo: String = "",
    var duracion: String = "",
    var video: Video = Video()
)
