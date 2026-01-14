//package com.example.mayoresfitmakers.ui.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.mayoresfitmakers.R
//import com.example.mayoresfitmakers.modelo.Evento
//
//class EventosAdapter(
//    private var eventos: List<Evento>,
//    private val onClick: (Evento) -> Unit
//) : RecyclerView.Adapter<EventosAdapter.EventoViewHolder>() {
//
//    class EventoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val txtTipo: TextView = itemView.findViewById(R.id.txtTipo)  //TODO: CAMBIAR POR NOMBRE REAL DE RECICLEVIEW
//        val txtLugar: TextView = itemView.findViewById(R.id.txtLugar) //TODO: CAMBIAR POR NOMBRE REAL DE RECICLEVIEW
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventoViewHolder {
//        val view: View = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_evento, parent, false)   //TODO: CAMBIAR POR NOMBRE REAL DE RECICLEVIEW
//        return EventoViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return eventos.size
//    }
//
//    override fun onBindViewHolder(holder: EventoViewHolder, position: Int) {
//        val evento: Evento = eventos[position]
//        holder.txtTipo.text = evento.tipo
//        holder.txtLugar.text = evento.lugar
//
//        holder.itemView.setOnClickListener {
//            onClick(evento)
//        }
//    }
//
//    fun updateList(newList: List<Evento>) {
//        eventos = newList
//        notifyDataSetChanged()
//    }
//
//    fun getItem(position: Int): Evento {
//        return eventos[position]
//    }
//}
