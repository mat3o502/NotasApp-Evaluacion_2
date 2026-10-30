package com.example.notasapp.ui.crear

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notasapp.databinding.ActivityCrearNotaBinding
import com.example.notasapp.network.RetrofitClient
import com.example.notasapp.repository.NotasRepository

class CrearNotaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearNotaBinding
    private lateinit var viewModel: CrearNotaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearNotaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = NotasRepository(RetrofitClient.getApi(this))
        viewModel = ViewModelProvider(this, CrearNotaViewModelFactory(repository))[CrearNotaViewModel::class.java]

        binding.btnGuardar.setOnClickListener {
            viewModel.guardar(
                binding.etTitulo.text.toString(),
                binding.etContenido.text.toString()
            )
        }

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is CrearNotaEstado.Inactivo -> mostrarCarga(false)
                is CrearNotaEstado.Cargando -> {
                    mostrarCarga(true)
                    binding.tvMensaje.visibility = View.GONE
                }
                is CrearNotaEstado.Exito -> {
                    mostrarCarga(false)
                    binding.etTitulo.text?.clear()
                    binding.etContenido.text?.clear()
                    mostrarMensaje("Nota guardada correctamente", Color.parseColor("#2E7D32"))
                    viewModel.reiniciarEstado()
                }
                is CrearNotaEstado.Error -> {
                    mostrarCarga(false)
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

    private fun mostrarCarga(cargando: Boolean) {
        binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        binding.btnGuardar.isEnabled = !cargando
    }
}