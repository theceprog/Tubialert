package com.proj.tubialert

import com.google.firebase.Timestamp

data class User(
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val phone: String = "",
    val gender: String = "",
    val createdAt: Timestamp? = null
)