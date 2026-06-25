package com.conectatarot.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.conectatarot.app.network.EspecialidadAdmin

class EspecialidadAdminAdapter(
    private val especialidades: List<EspecialidadAdmin>,
    private val onEditar: (EspecialidadAdmin) -> Unit,
    private val onEliminar: (Int) -> Unit
) : RecyclerView.Adapter<EspecialidadAdminAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre = view.findViewById<TextView>(R.id.tvNombre)
        val descripcion = view.findViewById<TextView>(R.id.tvDescripcion)
        val estado = view.findViewById<TextView>(R.id.tvEstado)
        val btnEditar = view.findViewById<Button>(R.id.btnEditar)
        val btnEliminar = view.findViewById<Button>(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_especialidad_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val especialidad = especialidades[position]

        holder.nombre.text = especialidad.nombre ?: ""
        holder.descripcion.text = especialidad.descripcion ?: "Sin descripción"

        val activa = especialidad.activa != false
        holder.estado.text = if (activa) "Estado: Activa" else "Estado: Inactiva"

        val id = especialidad.id ?: return

        holder.btnEditar.setOnClickListener {
            onEditar(especialidad)
        }

        holder.btnEliminar.setOnClickListener {
            onEliminar(id)
        }
    }

    override fun getItemCount(): Int = especialidades.size
}
