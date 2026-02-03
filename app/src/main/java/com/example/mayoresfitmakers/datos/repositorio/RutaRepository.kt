package com.example.mayoresfitmakers.datos.repositorio

import com.google.firebase.firestore.FirebaseFirestore
import org.osmdroid.util.GeoPoint

class RutaRepository {

    fun obtenerRuta(onResult: (GeoPoint, String?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("rutas")
            .document("ruta")
            .get()
            .addOnSuccessListener { doc ->

                val lat = doc.getDouble("latitud")
                val lng = doc.getDouble("longitud")
                val nombre = doc.getString("nombre")

                if (lat != null && lng != null) {
                    onResult(GeoPoint(lat, lng), nombre)
                }
            }
    }
}
