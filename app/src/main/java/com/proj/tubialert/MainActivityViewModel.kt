package com.proj.tubialert

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val firestoreService = FirestoreService()

    // Declaration: MutableLiveData for internal use, LiveData for external exposure
    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun loginUser(email: String, password: String) {
        _loginState.value = LoginState.Loading

        firestoreService.loginUser(
            email = email,
            password = password,
            onSuccess = { user ->
                _loginState.value = LoginState.Success(user)
            },
            onFailure = { exception ->
                _loginState.value = LoginState.Error(exception.message ?: "Login failed")
            }
        )
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User) : LoginState()
    data class Error(val message: String) : LoginState()
}