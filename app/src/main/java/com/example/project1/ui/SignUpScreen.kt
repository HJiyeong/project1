package com.example.project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project1.R
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.project1.model.UserCreateRequest
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signUpFailed by remember { mutableStateOf(false) }

    suspend fun onSignUpClick(name: String, email: String, password: String) {
        try {
            val request = UserCreateRequest(name = name, email = email, password = password)
            val response = RetrofitClient.apiService.signup(request)

            if (response != null) {
                signUpFailed = false
                navController.navigate("login")
            } else {
                signUpFailed = true
                android.util.Log.e("SignUp", " 응답이 null이야!")
            }
        } catch (e: Exception) {
            signUpFailed = true
            android.util.Log.e("SignUp", " 예외 발생: ${e.message}", e)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.beige))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("회원가입", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름", color = colorResource(R.color.brown)) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorResource(R.color.brown), cursorColor = colorResource(R.color.brown), focusedTextColor = colorResource(R.color.brown), unfocusedBorderColor = colorResource(R.color.brown), unfocusedTextColor = colorResource(R.color.brown)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일", color = colorResource(R.color.brown)) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorResource(R.color.brown), cursorColor = colorResource(R.color.brown), focusedTextColor = colorResource(R.color.brown), unfocusedBorderColor = colorResource(R.color.brown), unfocusedTextColor = colorResource(R.color.brown)),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("비밀번호", color = colorResource(R.color.brown)) },
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorResource(R.color.brown), cursorColor = colorResource(R.color.brown), focusedTextColor = colorResource(R.color.brown), unfocusedBorderColor = colorResource(R.color.brown), unfocusedTextColor = colorResource(R.color.brown)),
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = { coroutineScope.launch { onSignUpClick(name, email, password) }},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.brown))
        ) {
            Text("회원가입", color = Color.White)
        }

        if (signUpFailed) {
            Text("회원가입 실패. 다시 시도해주세요.", color = Color.Red, fontSize = 12.sp)
        }
    }
}