package com.proj.tubialert

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.proj.tubialert.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.signUpState.observe(this) { state ->
            when (state) {
                is SignUpState.Loading -> {
                    binding.signupbutton.isEnabled = false
                    // Show loading indicator
                }
                is SignUpState.Success -> {
                    binding.signupbutton.isEnabled = true
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    // Navigate to login or main activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignUpState.Error -> {
                    binding.signupbutton.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                SignUpState.Idle -> {
                    binding.signupbutton.isEnabled = true
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.signupbutton.setOnClickListener {
            val name = binding.nameedit.text.toString().trim()
            val email = binding.emailedit.text.toString().trim()
            val phone = binding.phoneedit.text.toString().trim()
            val gender = binding.genderedit.text.toString().trim()
            val password = binding.passwordedit.text.toString().trim()
            val confirmPassword = binding.passwordretypeedit.text.toString().trim()

            viewModel.signUpUser(name, email, phone, gender, password, confirmPassword)
        }

        binding.logintext.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetState()
    }
}