package com.example.notasapp.network

import android.content.Context
import com.example.notasapp.util.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // localhost + "adb reverse tcp:3000 tcp:3000" (celular fisico por Wi-Fi).
    // Si se usa el emulador de Android Studio, cambiar a http://10.0.2.2:3000/
    private const val BASE_URL = "http://localhost:3000/"

    @Volatile
    private var api: ApiService? = null

    fun getApi(context: Context): ApiService {
        return api ?: synchronized(this) {
            api ?: construir(context.applicationContext).also { api = it }
        }
    }

    private fun construir(context: Context): ApiService {
        val session = SessionManager(context)

        // Interceptor: agrega "Authorization: Bearer <token>" si hay sesion
        val authInterceptor = Interceptor { chain ->
            val builder = chain.request().newBuilder()
            session.getToken()?.let { builder.addHeader("Authorization", "Bearer $it") }
            chain.proceed(builder.build())
        }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}