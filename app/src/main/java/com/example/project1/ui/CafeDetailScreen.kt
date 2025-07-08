package com.example.project1.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil3.compose.AsyncImage
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.CafeInfo
import com.example.project1.model.PromptRequest
import com.example.project1.model.User
import com.example.project1.network.RetrofitClient
import com.example.project1.utils.getToken
import kotlinx.coroutines.launch

@Composable
fun CafeDetailScreen(
    navController: NavHostController,
    cafeInfo: CafeInfo
) {
    val token = getToken(LocalContext.current)

    var selectedTab by remember { mutableStateOf(0) }
    var isLiked by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isLiked = RetrofitClient.apiService.isFollowingCafe(cafeInfo.cid, token)
    }

    val coroutineScope = rememberCoroutineScope()

    val hashtagPrompt =
        """
            현재 카페에 대한 해시태그를 4개 작성해 줘.
            카페 이름은 다음과 같아 : $cafeInfo.name 
            ** 답변의 형식은 무조건 "#해시태그1 #해시태그2\n#해시태그3 #해시태그4"의 형식으로 작성해야 해.**
            다음은 가능한 해시태그들의 예시야 : #공부하기 좋은 #카이스트 도보 10분 #의자가 편한 #메뉴가 맛있는 등...
        """
    var hashTagResult by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            RetrofitClient.apiService.getHashTags(PromptRequest(hashtagPrompt))
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            model = cafeInfo.imageURL,
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
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Like",
                    tint = if (isLiked) Color.Red else Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            isLiked = true
                            coroutineScope.launch {
                                RetrofitClient.apiService.followCafe(cafeInfo.cid, token)
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(100.dp))

            Text(cafeInfo.name, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                if (hashTagResult.isNotEmpty()) hashTagResult else "",
                color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(36.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("위치 보기", "카페 정보", "관련 카페").forEachIndexed { index, label ->
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
                    0 -> LocationView(cafeInfo.shortAddress)
                    1 -> CafeInformation(cafeInfo.cafeIntroduce)
                    2 -> RelatedCafeView(navController, cafeInfo.name)
                }
            }
        }
    }
}

@Composable
fun LocationView(location: String) {
    Column {
        Text("📍 위치 정보", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(location, color = Color.White)
        Text("영업 종료 11:00에 영업 시작", color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.navermap_sample),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun CafeInformation(information: String) {
    Column {
        Text("☕️ 카페 정보:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = information,
            color = Color.White,
            fontSize = 14.sp,
            lineHeight = 22.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
    }
}

@Composable
fun RelatedCafeView(navController: NavHostController, name: String) {

    var promptResult by remember { mutableStateOf<List<CafeInfo>?>(null) }
    val prompt =
        """
               사용자의 조건은 다음과 같아.
               - 내가 찾으려는 카페는 : 네이버 지도 상에서 $name 과 가까운 카페여야 해.
               - 내가 찾으려는 카페는 : $name 과 관련이 있는 카페여야 해.
               
               조건에 맞는 카페 상위 15개를 추천해 줘.
               **답변은 무조건 네이버 지도 상에 실제 존재하는 카페 상호명을 기준으로,
               "이름1, 이름2, 이름3, 이름4, ..." 와 같은 형식으로 출력해 줘.**
        """

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            promptResult = RetrofitClient.apiService.recommendCafes(PromptRequest(prompt))
        }
    }

    Column {
        Text("🤍 관련 카페:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Text("비슷한 카페 입니다.", color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        val similarCafes = promptResult!!

        similarCafes.forEach {cafe ->
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
                    AsyncImage(
                        model = cafe.imageURL,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(cafe.name, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(cafe.shortAddress, color = Color.White)
                    }
                }
            }
        }
    }
}

