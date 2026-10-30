package com.example.notasapp.model

data class LoginRequest(val email: String, val password: String)

data class Usuario(val id: Int, val nombre: String, val email: String)

data class LoginResponse(val token: String, val usuario: Usuario)

data class NotaRequest(val titulo: String, val contenido: String)

data class Nota(
    val id: Int,
    val titulo: String,
    val contenido: String,
    val fecha_creacion: String
)

data class ErrorResponse(val mensaje: String?)