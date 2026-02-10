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
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val layoutRes: Int = R.layout.item_senderismo
        val view: View = inflater.inflate(layoutRes, parent, false)
        
        return CarruselViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
        val senderismo: Senderismo = senderismos[position]
        val context = holder.itemView.context
        
        holder.imageView.setImageDrawable(null) 
        
        Glide.with(context)
            .load(senderismo.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        val totalElements: Int = senderismos.size
        
        return totalElements
    }
}
