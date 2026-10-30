package com.example.notasapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notasapp.databinding.ActivityLoginBinding
import com.example.notasapp.network.RetrofitClient
import com.example.notasapp.repository.AuthRepository
import com.example.notasapp.ui.dashboard.DashboardActivity
import com.example.notasapp.util.SessionManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = SessionManager(this)
        // Si ya hay sesion guardada, ir directo al dashboard
        if (session.getToken() != null) {
            irAlDashboard()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = AuthRepository(RetrofitClient.getApi(this), session)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository))[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is LoginEstado.Inactivo -> mostrarCarga(false)
                is LoginEstado.Cargando -> {
                    mostrarCarga(true)
                    binding.tvError.visibility = View.GONE
                }
                is LoginEstado.Error -> {
                    mostrarCarga(false)
                    binding.tvError.text = estado.mensaje
                    binding.tvError.visibility = View.VISIBLE
                }
                is LoginEstado.Exito -> {
                    mostrarCarga(false)
                    irAlDashboard()
                }
            }
        }
    }

    private fun irAlDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    private fun mostrarCarga(cargando: Boolean) {
        binding.progressBar.visibility = if (cargando) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !cargando
    }
}