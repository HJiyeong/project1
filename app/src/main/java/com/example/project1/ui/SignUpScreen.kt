package com.example.project1.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.*
import androidx.core.content.edit
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.UserCreateRequest
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@Composable
fun SignUpScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var signUpFailed by remember { mutableStateOf(false) }

    var showNameField by remember { mutableStateOf(true) }
    var showEmailField by remember { mutableStateOf(false) }
    var showPasswordField by remember { mutableStateOf(false) }
    var showSignUpButton by remember { mutableStateOf(false) }

    suspend fun onSignUpClick(name: String, email: String, password: String) {
        try {
            val request = UserCreateRequest(name = name, email = email, password = password)
            val response = RetrofitClient.apiService.signup(request)

            if (response != null) {
                signUpFailed = false
                navController.navigate("login")
            } else {
                signUpFailed = true
            }
        } catch (e: Exception) {
            signUpFailed = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.beige))
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Column 내부 가장 위쪽에 추가
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = colorResource(R.color.brown)
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Text("환영합니다 👋", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5C3D2E))

        Spacer(modifier = Modifier.height(12.dp))

        // 이름 입력
        AnimatedVisibility(visible = showNameField, enter = fadeIn()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("이름을 입력해주세요", fontSize = 16.sp, color = Color(0xFF5C3D2E))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        if (it.isNotBlank()) showEmailField = true
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 이메일 입력
        AnimatedVisibility(visible = showEmailField, enter = fadeIn()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("이메일을 입력해주세요", fontSize = 16.sp, color = Color(0xFF5C3D2E))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (it.isNotBlank()) showPasswordField = true
                    },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 비밀번호 입력
        AnimatedVisibility(visible = showPasswordField, enter = fadeIn()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("비밀번호를 입력해주세요", fontSize = 16.sp, color = Color(0xFF5C3D2E))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (it.isNotBlank()) showSignUpButton = true
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 회원가입 버튼
        AnimatedVisibility(visible = showSignUpButton, enter = fadeIn()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            onSignUpClick(name, email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brown))
                ) {
                    Text("회원가입", color = Color.White)
                }

                if (signUpFailed) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("회원가입 실패. 다시 시도해주세요.", color = Color.Red, fontSize = 12.sp)
                }
            }
        }
    }
}
