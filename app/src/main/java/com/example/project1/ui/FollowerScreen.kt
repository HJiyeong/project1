package com.example.project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.project1.R
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import com.example.project1.model.*

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.LaunchedEffect
import coil3.request.ImageRequest
import com.example.project1.network.RetrofitClient
import com.example.project1.utils.getToken

@Composable
fun FollowerCafeCard(
    navController: NavHostController,
    cafeInfo: CafeInfo
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(120.dp)
            .height(140.dp)
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cafeInfo.imageURL)
                .build()
            ,
            contentDescription = cafeInfo.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    onClick = {
                        navController.navigate("cafeDetail/${cafeInfo.cid}")
                    })
        )

    }
}

@Composable
fun FollowerScreen( // FIN?
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf("팔로워") }

    val token = getToken(LocalContext.current)
    var loginUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(Unit) {
        loginUser = RetrofitClient.apiService.getCurrentUser(token)
    }
    if (loginUser == null) return
    val user = loginUser!!

    Log.d("FollowerScreen", "Current user: $user")
    val followers = user.followers

    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->
        if (followers.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.beige))
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                // 이미지 추가?
                Text(text = "아직 팔로워가 없습니다",
                    fontSize = 18.sp,
                    color = colorResource(R.color.brown)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "마음에 드는 유저를 팔로우해보세요!",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate("add_follower") },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.brown))
                ) { Text("팔로워 추가하기") }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(colorResource(R.color.beige))
                    .padding(innerPadding)
                    .padding(24.dp)
            ) {
                Text(
                    text = "팔로워 페이지",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                followers.forEach { user ->
                    Text(
                        text = user.name + "님의 pick"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        user.recommendation.forEach { cafeInfo ->
                            FollowerCafeCard(navController, cafeInfo)
                        }
                    }
                }
            }
        }
    }
}
