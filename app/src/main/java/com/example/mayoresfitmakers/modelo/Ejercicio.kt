package com.example.mayoresfitmakers.modelo

import com.google.firebase.firestore.PropertyName

data class Ejercicio(
    // El @PropertyName ayuda si en Firebase lo llamaste diferente, por seguridad
    @get:PropertyName("nombre") val nombre: String = "",
    @get:PropertyName("descripcion") val descripcion: String = "",
    @get:PropertyName("tipo") val tipo: String = "", // fuerza, aerobico, estirar
    @get:PropertyName("nivel") val nivel: String = "", // bajo, medio, alto
    @get:PropertyName("imagenUrl") val imagenUrl: String = "", // URL de la foto simulando video
    @get:PropertyName("duracion") val duracionSegundos: Int = 60 // Tiempo estimado por ejercicio
)