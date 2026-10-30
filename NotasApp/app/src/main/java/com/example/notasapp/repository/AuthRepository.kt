package com.example.notasapp.repository

import com.example.notasapp.model.LoginRequest
import com.example.notasapp.model.LoginResponse
import com.example.notasapp.network.ApiService
import com.example.notasapp.util.SessionManager
import java.io.IOException

class AuthRepository(
    private val api: ApiService,
    private val session: SessionManager
) {
    suspend fun login(email: String, password: String): Resultado<LoginResponse> {
        return try {
            val resultado = api.login(LoginRequest(email, password)).aResultado()
            if (resultado is Resultado.Exito) {
                session.guardarSesion(resultado.datos.token, resultado.datos.usuario.nombre)
            }
            resultado
        } catch (e: IOException) {
            Resultado.Error("No se pudo conectar con el servidor")
        } catch (e: Exception) {
            Resultado.Error("Ocurrio un error inesperado")
        }
    }
}