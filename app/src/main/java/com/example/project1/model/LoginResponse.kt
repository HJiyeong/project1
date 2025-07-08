package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("access_token") val token: String,
    @SerializedName("token_type") val type: String,
)