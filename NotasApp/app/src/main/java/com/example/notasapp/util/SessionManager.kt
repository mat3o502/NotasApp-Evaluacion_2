package com.example.notasapp.util

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("notasapp_session", Context.MODE_PRIVATE)

    fun guardarSesion(token: String, nombre: String) {
        prefs.edit()
            .putString(KEY_TOKEN, token)
            .putString(KEY_NOMBRE, nombre)
            .apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    fun getNombre(): String = prefs.getString(KEY_NOMBRE, "") ?: ""

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val KEY_NOMBRE = "nombre"
    }
}