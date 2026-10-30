package com.example.notasapp.repository

import com.example.notasapp.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class Resultado<out T> {
    data class Exito<T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}

fun <T> Response<T>.aResultado(): Resultado<T> {
    if (isSuccessful) {
        val cuerpo = body()
        return if (cuerpo != null) Resultado.Exito(cuerpo)
        else Resultado.Error("Respuesta vacia del servidor")
    }
    val mensaje = try {
        Gson().fromJson(errorBody()?.string(), ErrorResponse::class.java)?.mensaje
    } catch (e: Exception) {
        null
    }
    return Resultado.Error(mensaje ?: "Error del servidor (${code()})")
}