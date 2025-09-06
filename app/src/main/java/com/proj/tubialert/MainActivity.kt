package com.proj.tubialert

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.proj.tubialert.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Important: set the binding's root as content view

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()

        val forgotText: TextView = findViewById(R.id.forgot)
        forgotText.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }

        val signupText: TextView = findViewById(R.id.signuptext)
        signupText.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun setupObservers() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Loading -> {
                    binding.loginButton.isEnabled = false
                    // Show loading indicator if you have one
                }
                is LoginState.Success -> {
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to dashboard on successful login
                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish() // Optional: close login activity
                }
                is LoginState.Error -> {
                    binding.loginButton.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
                LoginState.Idle -> {
                    binding.loginButton.isEnabled = true
                }
            }
        }
    }}