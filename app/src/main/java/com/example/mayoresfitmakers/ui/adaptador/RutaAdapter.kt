package com.example.mayoresfitmakers.ui.adaptador

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mayoresfitmakers.databinding.ItemRutaBinding
import com.example.mayoresfitmakers.modelo.Ruta
import com.example.mayoresfitmakers.ui.MapActivity

class RutaAdapter(
    private val rutas: List<Ruta>
) : RecyclerView.Adapter<RutaAdapter.RutaViewHolder>() {

    inner class RutaViewHolder(val binding: ItemRutaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutaViewHolder {
        val binding = ItemRutaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RutaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RutaViewHolder, position: Int) {
        val ruta = rutas[position]

        holder.binding.txtNombre.text = ruta.nombre
        holder.binding.txtTipo.text = ruta.tipo

        holder.binding.btnIr.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MapActivity::class.java)

            intent.putExtra("lat", ruta.latitud)
            intent.putExtra("lng", ruta.longitud)
            intent.putExtra("nombre", ruta.nombre)

            context.startActivity(intent)
        }
    }

    override fun getItemCount() = rutas.size
}
