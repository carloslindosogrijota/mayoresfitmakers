package com.example.mayoresfitmakers.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mayoresfitmakers.R
import com.example.mayoresfitmakers.modelo.Evento

class EventosAdapter(
    private var eventos: List<Evento>,
    private val onClick: (Evento) -> Unit
) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {

    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEvento: ImageView = itemView.findViewById(R.id.imgEvento)
        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)
        val txtLugar: TextView = itemView.findViewById(R.id.txtLugar)
        val txtPlazas: TextView? = itemView.findViewById(R.id.txtPlazas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventoViewHolder(view)
    }

    override fun getItemCount(): Int = eventos.size

    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
        val evento = eventos[position]

        holder.txtTipo.text = evento.tipo
        holder.txtLugar.text = evento.lugar

        holder.txtPlazas?.text =
            "${evento.inscripciones} / ${evento.cupoMax} plazas"

        // Glide con URL
        if (evento.imagenUrl.isNotBlank()) {
            Glide.with(holder.itemView.context)
                .load(evento.imagenUrl)
                .centerCrop()
                .into(holder.imgEvento)
        } else {
            holder.imgEvento.setImageResource(R.drawable.ic_launcher_foreground)
        }

        holder.itemView.setOnClickListener {
            onClick(evento)
        }
    }

    fun updateList(nuevaLista: List<Evento>) {
        eventos = nuevaLista
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Evento = eventos[position]
}
