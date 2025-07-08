package com.example.project1.model
import com.google.gson.annotations.SerializedName

data class UserCreateResponse (
    val msg: String,
    @SerializedName("user_id") val uid: Int
)