package com.example.project1.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project1.model.LoginRequest
import com.example.project1.model.LoginResponse
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.core.content.edit
import com.example.project1.R


@Composable
fun LogInScreen(
    navController: NavHostController,
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginFailed by remember { mutableStateOf(false) }

    suspend fun onLoginClick(email: String, password: String) {
        try {
            val request = LoginRequest(email, password)
            val response: LoginResponse = RetrofitClient.apiService.login(request)

            val token = response.token
            val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            prefs.edit { putString("token", token) }

            loginFailed = false
            navController.navigate("home")
        } catch (e: Exception) {
            loginFailed = true
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
        Text("로그인", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("이메일", color = colorResource(R.color.brown))  },
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
            onClick = {
                coroutineScope.launch {
                    onLoginClick(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.brown))
        ) {
            Text("로그인")
        }

        if (loginFailed) {
            Text("로그인 실패. 다시 시도해주세요.", color = Color.Red, fontSize = 12.sp)
        }


        TextButton(onClick = { navController.navigate("signup") } ) {
            Text("회원가입", color = colorResource(R.color.brown))
        }
    }
}

