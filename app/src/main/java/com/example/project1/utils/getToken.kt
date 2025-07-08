package com.example.project1.utils
import android.content.Context

fun getToken(context: Context): String {
    val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
    val token = prefs.getString("token", null)
    return token.let { "Bearer $it" }
}