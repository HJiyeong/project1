package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class UserCreateRequest (
    val name: String,
    val email: String,
    val password: String
)