package com.example.mayoresfitmakers.modelo

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Ejercicio(
    // Campos que usábamos antes (los dejamos para no romper nada)
    @get:PropertyName("tipo") val tipo: String = "",
    @get:PropertyName("nivel") val nivel: String = "",
    @get:PropertyName("duracion") val duracionTexto: String = "",

    // Campos visuales que rellenamos nosotros a mano en el repositorio
    var nombre: String = "",
    var descripcion: String = "",
    var imagenUrl: String = "",

    // Campo ID para que no falle el código de tu compañero
    @get:Exclude var id: String = ""
)