package com.example.notasapp.network

import com.example.notasapp.model.LoginRequest
import com.example.notasapp.model.LoginResponse
import com.example.notasapp.model.Nota
import com.example.notasapp.model.NotaRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/notas")
    suspend fun crearNota(@Body request: NotaRequest): Response<Nota>

    @GET("api/notas")
    suspend fun obtenerNotas(): Response<List<Nota>>
}