package com.example.notasapp.ui.lista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.databinding.ItemNotaBinding
import com.example.notasapp.model.Nota
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private object NotaDiff : DiffUtil.ItemCallback<Nota>() {
    override fun areItemsTheSame(a: Nota, b: Nota) = a.id == b.id
    override fun areContentsTheSame(a: Nota, b: Nota) = a == b
}

class NotasAdapter : ListAdapter<Nota, NotasAdapter.NotaViewHolder>(NotaDiff) {

    class NotaViewHolder(val binding: ItemNotaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = ItemNotaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = getItem(position)
        holder.binding.tvTitulo.text = nota.titulo
        holder.binding.tvContenido.text = nota.contenido
        holder.binding.tvFecha.text = formatearFecha(nota.fecha_creacion)
    }

    // El servidor guarda la fecha en UTC; la mostramos en la hora del telefono
    private fun formatearFecha(fecha: String): String {
        return try {
            val entrada = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            val salida = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())
            salida.format(entrada.parse(fecha)!!)
        } catch (e: Exception) {
            fecha
        }
    }
}