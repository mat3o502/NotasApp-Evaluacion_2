package com.example.notasapp.repository

import com.example.notasapp.model.Nota
import com.example.notasapp.model.NotaRequest
import com.example.notasapp.network.ApiService
import java.io.IOException

class NotasRepository(private val api: ApiService) {

    suspend fun crearNota(titulo: String, contenido: String): Resultado<Nota> {
        return try {
            api.crearNota(NotaRequest(titulo, contenido)).aResultado()
        } catch (e: IOException) {
            Resultado.Error("No se pudo conectar con el servidor")
        } catch (e: Exception) {
            Resultado.Error("Ocurrio un error inesperado")
        }
    }

    suspend fun obtenerNotas(): Resultado<List<Nota>> {
        return try {
            api.obtenerNotas().aResultado()
        } catch (e: IOException) {
            Resultado.Error("No se pudo conectar con el servidor")
        } catch (e: Exception) {
            Resultado.Error("Ocurrio un error inesperado")
        }
    }
}