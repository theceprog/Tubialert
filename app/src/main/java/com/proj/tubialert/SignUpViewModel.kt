package com.proj.tubialert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    private val firestoreService = FirestoreService()

    private val _signUpState = MutableLiveData<SignUpState>(SignUpState.Idle)
    val signUpState: LiveData<SignUpState> = _signUpState

    fun signUpUser(
        name: String,
        email: String,
        phone: String,
        gender: String,
        password: String,
        confirmPassword: String
    ) {
        _signUpState.value = SignUpState.Loading

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
            gender.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _signUpState.value = SignUpState.Error("All fields are required")
            return
        }

        if (!firestoreService.isValidEmail(email)) {
            _signUpState.value = SignUpState.Error("Please enter a valid email address")
            return
        }

        if (!firestoreService.isValidPassword(password)) {
            _signUpState.value = SignUpState.Error("Password must be at least 8 characters with uppercase, lowercase, number, and symbol")
            return
        }

        if (password != confirmPassword) {
            _signUpState.value = SignUpState.Error("Passwords do not match")
            return
        }

        // Create user object
        val user = User(
            email = email,
            password = password, // Hash this in production
            name = name,
            phone = phone,
            gender = gender
        )

        // Register user
        firestoreService.signUpUser(
            user = user,
            onSuccess = {
                _signUpState.value = SignUpState.Success
            },
            onFailure = { exception ->
                _signUpState.value = SignUpState.Error(exception.message ?: "Sign up failed")
            }
        )
    }

    fun resetState() {
        _signUpState.value = SignUpState.Idle
    }
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}