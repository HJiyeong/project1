package com.example.project1.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.project1.R
import com.example.project1.model.User
import com.example.project1.network.RetrofitClient
import com.example.project1.utils.getToken
import kotlinx.coroutines.launch


@Composable
fun UserCard(user: User) {
    val token = getToken(LocalContext.current)
    var isFollowing by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 프로필 이미지
        Image(
            painter = rememberAsyncImagePainter(
                model = user.profileImage ?: "",
                error = painterResource(R.drawable.ic_leaf), // 기본 이미지. 이따 추가해야 함.
                fallback = painterResource(R.drawable.ic_leaf)
            ),
            contentDescription = "profile image",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = user.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        RetrofitClient.apiService.followUser(user.uid, token) // userId 인자 추가 필요
                        isFollowing = true
                    } catch (e: Exception) {
                        Log.e("Follow", "팔로우 실패: ${e.message}")
                    }
                }
                isFollowing = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.brown)),
            enabled = !isFollowing
        ) {
            Text(if (isFollowing) "팔로잉" else "팔로우")
        }
    }
} // FIN?

@Composable
fun AddFollower( // FIN?
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val token = getToken(context)
    var loginUser by remember { mutableStateOf<User?>(null) }
    var recommendedUsers by remember { mutableStateOf<List<User>>(emptyList()) }

    LaunchedEffect(Unit) {
        loginUser = RetrofitClient.apiService.getCurrentUser(token)
    }
    if (loginUser == null) return
    val user = loginUser!!

    var selectedTab by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorResource(R.color.beige))
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "팔로워 추가하기",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Log.d("TOKEN", "token: $token")
            LaunchedEffect(Unit) {
                recommendedUsers = RetrofitClient.apiService.recommendUsers(token)
            }

            recommendedUsers.forEach { recommendedUser ->
                UserCard(recommendedUser)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}