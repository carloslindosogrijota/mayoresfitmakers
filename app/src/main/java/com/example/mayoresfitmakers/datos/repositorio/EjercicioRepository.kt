package com.example.mayoresfitmakers.datos.repositorio

import com.example.mayoresfitmakers.modelo.Ejercicio
import kotlinx.coroutines.delay

class EjercicioRepository {

    // NO USAMOS FIREBASE AHORA
    // private val db = FirebaseFirestore.getInstance()

    suspend fun obtenerRutinaPersonalizada(tipo: String, nivel: String, tiempoTotalMinutos: Int): List<Ejercicio> {

        // 1. Simulamos una carga de red (opcional, para ver si sale el spinner o carga rápido)
        delay(500) // medio segundo de espera falsa

        // 2. CREAMOS NUESTRA BASE DE DATOS FALSA AQUÍ MISMO
        val baseDeDatosFalsa = listOf(
            // --- FUERZA / BAJO ---
            Ejercicio("Levantamiento Silla", "Siéntate y levántate despacio", "fuerza", "bajo", "", 60),
            Ejercicio("Bíceps con Botella", "Usa una botella de agua como pesa", "fuerza", "bajo", "", 60),
            Ejercicio("Prtes Pared", "Flexiones apoyado en la pared", "fuerza", "bajo", "", 60),
            Ejercicio("Elevación Lateral", "Levanta los brazos en cruz", "fuerza", "bajo", "", 60),

            // --- FUERZA / MEDIO ---
            Ejercicio("Sentadilla Profunda", "Baja hasta abajo", "fuerza", "medio", "", 60),
            Ejercicio("Plancha Abdominal", "Aguanta recto 30 seg", "fuerza", "medio", "", 60),

            // --- AEROBICO / BAJO ---
            Ejercicio("Marcha en el sitio", "Camina sin moverte", "aerobico", "bajo", "", 60),
            Ejercicio("Pasos laterales", "Un paso a la derecha, otro a la izquierda", "aerobico", "bajo", "", 60),

            // --- ESTIRAR / BAJO ---
            Ejercicio("Estirar Cuello", "Mueve la cabeza suavemente", "estirar", "bajo", "", 60),
            Ejercicio("Abrazo al pecho", "Lleva la rodilla al pecho", "estirar", "bajo", "", 60)
        )

        // 3. FILTRAMOS MANUALMENTE (Lo que antes hacía Firebase con 'whereEqualTo')
        // Buscamos en la lista falsa los que coincidan con lo que pidió el usuario
        val ejerciciosFiltrados = baseDeDatosFalsa.filter {
            it.tipo == tipo && it.nivel == nivel
        }

        // 4. ALGORITMO DE TIEMPO (Igual que antes)
        val rutinaGenerada = ArrayList<Ejercicio>()
        var tiempoAcumulado = 0
        val tiempoTotalSegundos = tiempoTotalMinutos * 60

        // Si no encontramos nada, devolvemos lista vacía
        if (ejerciciosFiltrados.isEmpty()) return emptyList()

        // Barajamos y rellenamos hasta completar el tiempo
        // Usamos un bucle infinito (while) para repetir ejercicios si la lista es corta
        // hasta llenar el tiempo del usuario.
        while (tiempoAcumulado < tiempoTotalSegundos) {
            for (ejercicio in ejerciciosFiltrados.shuffled()) {
                if (tiempoAcumulado + ejercicio.duracionSegundos <= tiempoTotalSegundos) {
                    rutinaGenerada.add(ejercicio)
                    tiempoAcumulado += ejercicio.duracionSegundos
                }
                if (tiempoAcumulado >= tiempoTotalSegundos) break
            }
            // Seguridad para no colgarse si no hay ejercicios que quepan
            if (rutinaGenerada.isEmpty()) break
        }

        return rutinaGenerada
    }
}