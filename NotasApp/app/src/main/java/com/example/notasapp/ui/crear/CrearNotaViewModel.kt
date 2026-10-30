package com.example.notasapp.ui.crear

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notasapp.repository.NotasRepository
import com.example.notasapp.repository.Resultado
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de crear nota
sealed class CrearNotaEstado {
    object Inactivo : CrearNotaEstado()
    object Cargando : CrearNotaEstado()
    object Exito : CrearNotaEstado()
    data class Error(val mensaje: String) : CrearNotaEstado()
}

class CrearNotaViewModel(private val repository: NotasRepository) : ViewModel() {

    private val _estado = MutableLiveData<CrearNotaEstado>(CrearNotaEstado.Inactivo)
    val estado: LiveData<CrearNotaEstado> = _estado

    fun guardar(titulo: String, contenido: String) {
        if (titulo.isBlank() || contenido.isBlank()) {
            _estado.value = CrearNotaEstado.Error("Escribe un título y un contenido")
            return
        }

        _estado.value = CrearNotaEstado.Cargando

        viewModelScope.launch {
            when (val resultado = repository.crearNota(titulo.trim(), contenido.trim())) {
                is Resultado.Exito -> _estado.value = CrearNotaEstado.Exito
                is Resultado.Error -> _estado.value = CrearNotaEstado.Error(resultado.mensaje)
            }
        }
    }

    // Se llama despues de mostrar el exito, para no repetirlo al rotar la pantalla
    fun reiniciarEstado() {
        _estado.value = CrearNotaEstado.Inactivo
    }
}

class CrearNotaViewModelFactory(
    private val repository: NotasRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrearNotaViewModel(repository) as T
    }
}