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
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.project1.R
import com.example.project1.data.UserManager
import com.example.project1.model.*

import android.util.Log

@Composable
fun followerCafeCard(
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
            model = cafeInfo.imageURL,
            contentDescription = cafeInfo.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
                .clickable(
                    onClick = {
                        navController.navigate("cafeinfo/${cafeInfo.cid}")
                    })
        )

    }
}

@Composable
fun FollowerScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf("팔로워") }
    val user = UserManager.currentUser ?: return
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
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
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
                ){
                    user.recommendation.forEach { cafeInfo ->
                        followerCafeCard(navController, cafeInfo)
                    }
                }
            }
        }
    }
}
