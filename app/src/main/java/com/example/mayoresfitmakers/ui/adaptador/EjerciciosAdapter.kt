package com.example.mayoresfitmakers.ui.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Ejercicio

// EL CAMBIO ESTÁ AQUÍ: El nombre de la clase debe ser igual que el del archivo
class EjerciciosAdapter(private val listaEjercicios: List<Ejercicio>) :
    RecyclerView.Adapter<EjerciciosAdapter.EjercicioViewHolder>() {

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
        val minutos = item.duracionSegundos / 60
        holder.duracion.text = "⏳ Duración: $minutos min aprox"
    }

    override fun getItemCount() = listaEjercicios.size
}