package com.example.mayoresfitmakers.ui.adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Senderismo
import com.bumptech.glide.Glide


class SenderismoAdapter(private val senderismos: List<Senderismo>) :
    RecyclerView.Adapter<SenderismoAdapter.CarruselViewHolder>() {

    class CarruselViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.senderismoImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarruselViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_senderismo, parent, false)
        return CarruselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
        val senderismo = senderismos[position]
        
        // Limpiar la imagen anterior para evitar problemas de reciclaje
        holder.imageView.setImageDrawable(null)
        
        // Cargar imagen desde URL usando Glide
        Glide.with(holder.itemView.context)
            .load(senderismo.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background) // Placeholder mientras carga
            .error(R.drawable.ic_launcher_foreground) // Imagen de error si falla
            .into(holder.imageView)
    }

    override fun getItemCount() = senderismos.size
}