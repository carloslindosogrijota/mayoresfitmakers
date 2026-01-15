package com.example.proyectopruebas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Senderismo


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
        holder.imageView.setImageResource(senderismos[position].imageResId)
    }

    override fun getItemCount() = senderismos.size
}