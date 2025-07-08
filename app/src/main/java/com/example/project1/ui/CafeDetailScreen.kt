package com.example.project1.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.CafeInfo

@Composable
fun CafeDetailScreen(
    navController: NavHostController,
    cafeInfo: CafeInfo
) {
    var selectedTab by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_cafe_sample6),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFF4E342E))
                    )
                )
        )

        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Icon(Icons.Default.Close, contentDescription = "닫기", tint = Color.White,
                    modifier = Modifier.size(32.dp).clickable { navController.popBackStack() })
                Icon(Icons.Default.FavoriteBorder, contentDescription = "찜", tint = Color.White, modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(100.dp))

            Text("XXXXXXXX카페", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("#공부하기 좋은 #카이스트 도보 10분\n#의자가 편한", color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("대전광역시 유성구 죽동", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("위치 보기", "메뉴 보기", "관련 카페").forEachIndexed { index, label ->
                    Button(
                        onClick = { selectedTab = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == index) Color(0xFF4E342E) else Color(0x554E342E),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text(label) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    0 -> LocationView()
                    1 -> MenuView()
                    2 -> RelatedCafeView(navController)
                }
            }
        }
    }
}

@Composable
fun LocationView() {
    Column {
        Text("📍 위치 정보", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("대전 유성구 죽동로279번길 40 1층", color = Color.White)
        Text("영업 종료 11:00에 영업 시작", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("지도 API", color = Color.White)
        }
    }
}

@Composable
fun MenuView() {
    Column {
        Text("☕️ 메뉴 정보:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        val menuImages = listOf(
            R.drawable.img_cafe_sample3,
            R.drawable.img_cafe_sample2,
            R.drawable.img_cafe_sample1
        )

        LazyColumn {
            items(menuImages.size) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xAA4E342E))
                ) {
                    Image(
                        painter = painterResource(id = menuImages[index]),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .height(140.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text("아이스 아메리카노", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("4,500원", color = Color.White)
                    }
                }
            }
        }
    }
}
@Composable
fun RelatedCafeView(navController: NavHostController) {
    val cafes = listOf(
        Triple("카페 산책", "대전시 유성구", R.drawable.img_cafe_sample3),
        Triple("무드 인 카페", "대전시 서구", R.drawable.img_cafe_sample2),
        Triple("카페 모노톤", "대전시 동구", R.drawable.img_cafe_sample1)
    )

    LazyColumn {
        item {
            Text("🤍 관련 카페:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
            Text("공부하기 좋은 비슷한 카페 입니다.", color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(cafes.size) { index ->
            val (name, location, imageId) = cafes[index]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("CafeDetail")
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xAA4E342E))
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    Image(
                        painter = painterResource(id = imageId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(name, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(location, color = Color.White)
                    }
                }
            }
        }
    }
}

