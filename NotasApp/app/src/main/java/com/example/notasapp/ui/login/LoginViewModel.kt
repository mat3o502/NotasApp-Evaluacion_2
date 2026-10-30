package com.example.notasapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notasapp.repository.AuthRepository
import com.example.notasapp.repository.Resultado
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de login
sealed class LoginEstado {
    object Inactivo : LoginEstado()
    object Cargando : LoginEstado()
    data class Exito(val nombre: String) : LoginEstado()
    data class Error(val mensaje: String) : LoginEstado()
}

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _estado = MutableLiveData<LoginEstado>(LoginEstado.Inactivo)
    val estado: LiveData<LoginEstado> = _estado

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _estado.value = LoginEstado.Error("Ingresa tu correo y contraseña")
            return
        }

        _estado.value = LoginEstado.Cargando

        viewModelScope.launch {
            when (val resultado = repository.login(email.trim(), password)) {
                is Resultado.Exito -> _estado.value =
                    LoginEstado.Exito(resultado.datos.usuario.nombre)
                is Resultado.Error -> _estado.value =
                    LoginEstado.Error(resultado.mensaje)
            }
        }
    }
}

// Fabrica para pasarle el repositorio al ViewModel
class LoginViewModelFactory(
    private val repository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(repository) as T
    }
}