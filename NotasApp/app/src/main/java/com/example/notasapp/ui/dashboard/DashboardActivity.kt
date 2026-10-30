package com.example.notasapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.notasapp.databinding.ActivityDashboardBinding
import com.example.notasapp.ui.crear.CrearNotaActivity
import com.example.notasapp.ui.lista.ListaNotasActivity
import com.example.notasapp.ui.login.LoginActivity
import com.example.notasapp.util.SessionManager

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val session = SessionManager(this)
        binding.tvBienvenida.text = "Bienvenido, ${session.getNombre()}"

        binding.btnCrearNota.setOnClickListener {
            startActivity(Intent(this, CrearNotaActivity::class.java))
        }

        binding.btnVerNotas.setOnClickListener {
            startActivity(Intent(this, ListaNotasActivity::class.java))
        }

        binding.btnCerrarSesion.setOnClickListener {
            session.cerrarSesion()

            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            startActivity(intent)
        }
    }
}