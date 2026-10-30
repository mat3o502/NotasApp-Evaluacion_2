package com.example.notasapp.ui.lista

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notasapp.model.Nota
import com.example.notasapp.repository.NotasRepository
import com.example.notasapp.repository.Resultado
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de listado
sealed class ListaEstado {
    object Cargando : ListaEstado()
    data class Exito(val notas: List<Nota>) : ListaEstado()
    data class Error(val mensaje: String) : ListaEstado()
}

class ListaNotasViewModel(private val repository: NotasRepository) : ViewModel() {

    private val _estado = MutableLiveData<ListaEstado>(ListaEstado.Cargando)
    val estado: LiveData<ListaEstado> = _estado

    init {
        cargarNotas()
    }

    fun cargarNotas() {
        _estado.value = ListaEstado.Cargando
        viewModelScope.launch {
            when (val resultado = repository.obtenerNotas()) {
                is Resultado.Exito -> _estado.value = ListaEstado.Exito(resultado.datos)
                is Resultado.Error -> _estado.value = ListaEstado.Error(resultado.mensaje)
            }
        }
    }
}

class ListaNotasViewModelFactory(
    private val repository: NotasRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListaNotasViewModel(repository) as T
    }
}