package com.example.mayoresfitmakers.ui.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Ejercicio

class EjerciciosAdapter(private val listaEjercicios: List<Ejercicio>) :
    RecyclerView.Adapter<EjerciciosAdapter.EjercicioViewHolder>() {

    // LISTA DE IMÁGENES FALSAS (Asegúrate de que se llamen img1, img2... en drawable)
    private val imagenesDisponibles = listOf(
        R.drawable.img1,
        R.drawable.img2,
        R.drawable.img3,
        R.drawable.img4
    )

    class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.txtNombreEjercicio)
        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val duracion: TextView = view.findViewById(R.id.txtDuracion)
        val imagen: ImageView = view.findViewById(R.id.imgEjercicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ejercicio, parent, false)
        return EjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val item = listaEjercicios[position]

        holder.titulo.text = item.nombre
        holder.descripcion.text = item.descripcion
        holder.duracion.text = "⏳ ${item.duracionTexto}"

        // TRUCO DE IMAGEN:
        // Si es el ejercicio 0 -> pone img1
        // Si es el ejercicio 1 -> pone img2
        // ... Si se acaban, vuelve a empezar (con el operador %)
        val imagenParaEste = imagenesDisponibles[position % imagenesDisponibles.size]
        holder.imagen.setImageResource(imagenParaEste)
    }

    override fun getItemCount() = listaEjercicios.size
}