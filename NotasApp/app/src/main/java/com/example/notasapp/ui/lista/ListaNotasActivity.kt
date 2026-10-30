package com.example.notasapp.ui.lista

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notasapp.databinding.ActivityListaNotasBinding
import com.example.notasapp.network.RetrofitClient
import com.example.notasapp.repository.NotasRepository

class ListaNotasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaNotasBinding
    private lateinit var viewModel: ListaNotasViewModel
    private val adapter = NotasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaNotasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvNotas.adapter = adapter

        val repository = NotasRepository(RetrofitClient.getApi(this))
        viewModel = ViewModelProvider(this, ListaNotasViewModelFactory(repository))[ListaNotasViewModel::class.java]

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is ListaEstado.Cargando -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvMensaje.visibility = View.GONE
                }
                is ListaEstado.Exito -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.submitList(estado.notas)
                    if (estado.notas.isEmpty()) {
                        mostrarMensaje("Aún no tienes notas guardadas", Color.parseColor("#9E9E9E"))
                    } else {
                        binding.tvMensaje.visibility = View.GONE
                    }
                }
                is ListaEstado.Error -> {
                    binding.progressBar.visibility = View.GONE
                    mostrarMensaje(estado.mensaje, Color.parseColor("#D32F2F"))
                }
            }
        }
    }

    private fun mostrarMensaje(texto: String, color: Int) {
        binding.tvMensaje.text = texto
        binding.tvMensaje.setTextColor(color)
        binding.tvMensaje.visibility = View.VISIBLE
    }
}