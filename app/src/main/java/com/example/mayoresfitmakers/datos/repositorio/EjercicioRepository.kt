package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.Ejercicio
import kotlinx.coroutines.delay

class EjercicioRepository {

    // Simular llamada a internet (se puede quitar el delay si quieres que sea instantáneo)
    suspend fun obtenerRutinaPersonalizada(tipo: String, nivel: String, tiempo: Int): List<Ejercicio> {
        // Simulamos una pequeña espera para que parezca real
        delay(500)

        val listaGenerada = ArrayList<Ejercicio>()

        // Generamos ejercicios "fake" pero bonitos según lo que haya pedido el usuario
        val categoria = tipo.uppercase()

        // EJERCICIO 1
        listaGenerada.add(Ejercicio(
            tipo = tipo,
            nivel = nivel,
            duracionTexto = "5 min",
            nombre = "Calentamiento: $categoria",
            descripcion = "Empezamos moviendo las articulaciones suavemente. Respira hondo y prepárate.",
            id = "mock1"
        ))

        // EJERCICIO 2
        listaGenerada.add(Ejercicio(
            tipo = tipo,
            nivel = nivel,
            duracionTexto = "10 min",
            nombre = "Ejercicio Principal 1",
            descripcion = "Mantén la espalda recta. Sigue el ritmo del vídeo sin forzar. Si te cansas, para.",
            id = "mock2"
        ))

        // EJERCICIO 3
        listaGenerada.add(Ejercicio(
            tipo = tipo,
            nivel = nivel,
            duracionTexto = "10 min",
            nombre = "Ejercicio Principal 2",
            descripcion = "Ahora aumentamos un poco la intensidad. Recuerda beber agua si lo necesitas.",
            id = "mock3"
        ))

        // EJERCICIO 4
        listaGenerada.add(Ejercicio(
            tipo = tipo,
            nivel = nivel,
            duracionTexto = "5 min",
            nombre = "Vuelta a la calma",
            descripcion = "Terminamos con movimientos lentos para relajar los músculos. ¡Buen trabajo!",
            id = "mock4"
        ))

        return listaGenerada
    }
}