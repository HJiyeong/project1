package com.example.project1.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextFieldDefaults
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
import coil3.request.ImageRequest
import coil3.compose.rememberAsyncImagePainter
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
    //LaunchedEffect(Unit) {
        //isLiked = RetrofitClient.apiService.isFollowingCafe(cafeInfo.cid, token)
    //}

    val coroutineScope = rememberCoroutineScope()

    //val hashtagPrompt =
        //"""
            //현재 카페에 대한 해시태그를 4개 작성해 줘.
            //카페 이름은 다음과 같아 : $cafeInfo.name
            //** 답변의 형식은 무조건 "#해시태그1 #해시태그2\n#해시태그3 #해시태그4"의 형식으로 작성해야 해.**
            //다음은 가능한 해시태그들의 예시야 : #공부하기 좋은 #카이스트 도보 10분 #의자가 편한 #메뉴가 맛있는 등...
        //"""
    //var hashTagResult by remember { mutableStateOf("") }
    //LaunchedEffect(Unit) {
        //coroutineScope.launch {
            //RetrofitClient.apiService.getHashTags(PromptRequest(hashtagPrompt))
        //}
    //}

    val hashTagResult = "#분위기 좋은 #자리에 앉기 좋음\n#사진 맛집 #조용한 카페"

    LaunchedEffect(Unit) {
        println("✅ cafeInfo.imageURL: ${cafeInfo.imageURL}")
    }


    val rawUrl = cafeInfo.imageURL
    val decodedOnce = java.net.URLDecoder.decode(rawUrl.substringAfter("src="), "UTF-8")
    val decodedTwice = java.net.URLDecoder.decode(decodedOnce.substringAfter("src="), "UTF-8")
    val imageUrl = decodedTwice

    LaunchedEffect(Unit) {
        println("🔍 Raw URL: $rawUrl")
        println("🔍 Decoded Once: $decodedOnce")
        println("🔍 Decoded Twice (최종 imageUrl): $imageUrl")
    }

    val painter = rememberAsyncImagePainter(model = imageUrl)



    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f))) {
        Image(
            painter = rememberAsyncImagePainter(cafeInfo.imageURL),  // 디코딩 없이 그대로
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.zIndex(0f).fillMaxSize()

        )


        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFF4E342E))
                    )
                )
        )

        Column(modifier = Modifier.zIndex(2f).fillMaxSize().padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "닫기",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.popBackStack() }
                )
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

            var searchQuery by remember { mutableStateOf("") }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("검색어를 입력하세요") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    cursorColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.4f),
                    disabledIndicatorColor = Color.Gray
                )


            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(cafeInfo.name, fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(hashTagResult, color = Color.White, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(36.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
                    .fillMaxHeight(0.7f)  // ✅ 하단 1/3만 차지
                    .background(Color.Black.copy(alpha = 0.4f))
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    when (selectedTab) {
                        0 -> LocationView(
                            address = cafeInfo.shortAddress,
                            naverMapUrl = cafeInfo.cafeURL
                        )
                        1 -> CafeInformation(
                            introduce = cafeInfo.cafeIntroduce,
                            amenities = cafeInfo.amenities
                        )
                        2 -> RelatedCafeView(navController, cafeInfo.name)
                    }
                }
            }

        }
    }
}

@Composable
fun LocationView(address: String, naverMapUrl: String) {
    val context = LocalContext.current

    Column {
        Text("📍 위치 정보", color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(address, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                intent.data = android.net.Uri.parse(naverMapUrl)
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4E342E), // 브라운 톤
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp) // 조금 더 세련되게
        ) {
            Text("네이버 지도로 열기")
        }

    }
}


@Composable
fun CafeInformation(introduce: String?, amenities: String?) {
    val noInfo = introduce.isNullOrBlank() && amenities.isNullOrBlank()

    Column {
        Text("☕️ 카페 정보:", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        if (noInfo) {
            Text(
                text = "정보가 없습니다.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        } else {
            if (!introduce.isNullOrBlank()) {
                Text(
                    text = introduce,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (!amenities.isNullOrBlank()) {
                Text(
                    text = "📌 추가 서비스",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.White
                )
                Text(
                    text = amenities,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 4.dp)
                )
            }
        }
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
                        model = ImageRequest.Builder(LocalContext.current).data(cafe.imageURL),
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

