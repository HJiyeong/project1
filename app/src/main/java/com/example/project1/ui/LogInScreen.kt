package com.example.project1.ui

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.LoginRequest
import com.example.project1.model.LoginResponse
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LogInScreen(navController: NavHostController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var showSplash by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginFailed by remember { mutableStateOf(false) }

    var showSignupGuide by remember { mutableStateOf(false) }
    var showEmailField by remember { mutableStateOf(false) }
    var showPasswordField by remember { mutableStateOf(false) }
    var showLoginButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(2000)
        showSplash = false
        delay(300)
        showSignupGuide = true
        delay(300)
        showEmailField = true
    }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.beige)),
        contentAlignment = Alignment.Center
    ) {
        // 로고 스플래시
        AnimatedVisibility(visible = showSplash, enter = fadeIn(), exit = fadeOut()) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "앱 로고",
                modifier = Modifier.size(180.dp)
            )
        }

        // 로그인 단계
        // 내부 Column만 수정합니다. 기존 코드에 이대로 교체하세요

        AnimatedVisibility(visible = !showSplash, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // 상단 배치
            ) {
                Spacer(modifier = Modifier.height(48.dp))

                // ✅ 상단 로고
                Image(
                    painter = painterResource(R.drawable.ic_logo),
                    contentDescription = "로고",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 24.dp)
                )

                // ✅ 회원가입 안내
                AnimatedVisibility(visible = showSignupGuide, enter = fadeIn()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("혹시 계정이 없으신가요?", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = { navController.navigate("signup") }) {
                            Text("회원가입", color = colorResource(R.color.brown))
                        }
                    }
                }

                // ✅ 이메일 입력
                AnimatedVisibility(visible = showEmailField, enter = fadeIn()) {
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text("로그인을 위해 이메일을 입력해주세요", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF5C3D2E))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                if (it.isNotBlank()) showPasswordField = true
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        )
                    }
                }

                // ✅ 비밀번호 입력
                AnimatedVisibility(visible = showPasswordField, enter = fadeIn()) {
                    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("비밀번호를 입력해주세요", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF5C3D2E))
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (it.isNotBlank()) showLoginButton = true
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                        )
                    }
                }

                // ✅ 로그인 버튼
                AnimatedVisibility(visible = showLoginButton, enter = fadeIn()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    onLoginClick(email, password)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brown))
                        ) {
                            Text("로그인", fontSize = 16.sp, color = Color.White)
                        }

                        if (loginFailed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "로그인 실패. 다시 시도해주세요.",
                                color = Color.Red,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

    }
}
