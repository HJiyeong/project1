package com.example.project1.ui
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.project1.R
import com.example.project1.model.CafeInfo
import com.example.project1.model.PromptRequest
import com.example.project1.network.RetrofitClient
import com.example.project1.viewmodel.CafeListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel as ViewModel

data class CafeItem(
    val title: String,
    val tags: List<String>,
    val count: String,
    val imageRes: Int
)


data class Question(
    val emoji: String,
    val question: String,
    val hint: String = "입력해주세요..."
)


data class Recommendation(val title: String, val description: String, val imageRes: Int)

val recommendationList = listOf(
    Recommendation("스타벅스", "공부하기 최적의 장소", R.drawable.img_cafe_sample1),
    Recommendation("메이크어케이크", "브런치가 맛있는 곳", R.drawable.img_cafe_sample2),
    Recommendation("빽다방", "가성비 챙기세요~", R.drawable.img_cafe_sample3)
)

data class InfoItem(val title: String, val content: String)

data class CafeListItem(
    val title: String,
    val username: String,
    val imageRes: Int,

)


val trendingCafeLists = listOf(
    CafeListItem("카공족을 위한", "@사용자1", R.drawable.img_cafe_sample4),
    CafeListItem("분좋카 모음", "@사용자2", R.drawable.img_cafe_sample5),
    CafeListItem("고양이를 볼 수 있는", "@사용자3", R.drawable.img_cafe_sample6),
    CafeListItem("이국적인 메뉴가 있는", "@사용자4", R.drawable.img_cafe_sample7)
)

@Composable
fun TypewriterText(
    text: String,
    speed: Long = 50L, // 타자 속도 (ms)
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current
) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        visibleText = ""
        for (i in text.indices) {
            visibleText += text[i]
            delay(speed)
        }
    }

    Text(
        text = visibleText,
        style = style,
        modifier = modifier
    )
}



@Composable
fun RecommendationCard(item: Recommendation) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(end = 8.dp)
            .width(160.dp)
            .height(220.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(item.title, fontWeight = FontWeight.Bold)
                Text(item.description, fontSize = 12.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.BookmarkBorder,
                        contentDescription = "Scrap Icon",
                        tint = Color(0xFF7A4E2D),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${(10..99).random()}", // 임시로 랜덤
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

            }
        }
    }
}
@Composable
fun CafeCard(
    cafeInfo: CafeInfo,
    onClick: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            Color(0x994E342E),  // 중간 정도
            Color(0xFF4E342E)   // 완전 진한 갈색

        ),
        startY = 200f,
        endY = 600f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }  // ✅ 클릭 이벤트
    ) {
        Image(
            painter = rememberAsyncImagePainter(cafeInfo.imageURL),
            contentDescription = cafeInfo.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cafeInfo.name,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cafeInfo.shortAddress,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 13.sp
                )
            }

            IconButton(
                onClick = { /* TODO: 저장 기능 */ },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.BookmarkBorder,
                    contentDescription = "저장",
                    tint = Color.White
                )
            }
        }
    }
}




@Composable
fun CurationScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf("카페 큐레이션") }
    var selectedFilter by remember { mutableStateOf("전체 검색") }
    var searchText by remember { mutableStateOf("") }
    var prompt by remember { mutableStateOf("") }
    var promptResult by remember { mutableStateOf<List<CafeInfo>?>(null) }

    val beige = colorResource(R.color.beige)
    val brown = Color(0xFF7A4E2D)
    val viewModel: CafeListViewModel = viewModel()
    val defaultCafes by viewModel.defaultCafes.collectAsState()


    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(beige)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = "카페 큐레이션",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "오늘의 당신의 카페를 찾아보세요",
                fontSize = 14.sp,
                color = brown.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("전체 검색", "맞춤형", "요즘 핫한").forEach { label ->
                    Button(
                        onClick = { selectedFilter = label },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (label == selectedFilter) brown else Color.Transparent
                        ),
                        shape = RoundedCornerShape(30.dp),
                        border = BorderStroke(1.dp, brown),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = label,
                            color = if (label == selectedFilter) Color.White else brown,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // 기본 카페 목록 불러오기 (디버깅)
            LaunchedEffect(Unit) {
                println("✅ fetchDefaultCafes 호출 전")
                viewModel.fetchDefaultCafes()
                println("✅ fetchDefaultCafes 호출 완료, 값: ${viewModel.defaultCafes.value}")
            }

            // ✅ 조건에 따라 카페 리스트 보여주기
            when (selectedFilter) {
                "전체 검색" -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(defaultCafes) { cafe ->
                            CafeCard(cafeInfo = cafe) {
                                navController.navigate("cafeDetail/${cafe.cid}")
                            }
                        }

                    }
                }

                "맞춤형" -> {
                    // TODO: 맞춤형 질문 리스트 표시
                    PersonalizedQuestionStack()
                }

                "요즘 핫한" -> {
                    // TODO: 핫한 카페 리스트 뷰
                    HotNowScreen()
                }
            }
        }
    }

    }




@Composable
fun PersonalizedQuestionStack() {
    val questionList = listOf(
        Question("\uD83D\uDDFA\uFE0F", "어디살아요??????"),
        Question("😊", "오늘의 기분은 어떠세요?"),
        Question("☕", "오늘 뭐하실 예정이세요?"),
        Question("☁️", "오늘의 날씨는 어때요?"),
        Question("👫", "누구와 함께 가시나요?"),
        Question("❓", "참고할만한 사항이 있을까요?")
    )

    val answers = remember { mutableStateListOf<String>() }
    var promptResult by remember { mutableStateOf<List<CafeInfo>?>(null) }

    Column {
        for (i in questionList.indices) {
            if (i == 0 || answers.size >= i) {
                val question = questionList[i]
                var input by remember { mutableStateOf("") }

                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8E3B6))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = question.emoji,
                                fontSize = 28.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )

                            // ✅ 질문 텍스트에 타자 애니메이션 적용
                            if (answers.size == i) {
                                TypewriterText(
                                    text = question.question,
                                    speed = 40L,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Color(0xFF5C3D2E)
                                    )
                                )
                            } else {
                                Text(
                                    text = question.question,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF5C3D2E)
                                )
                            }
                        }

                        if (answers.size > i) {
                            Text(
                                text = answers[i],
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } else {
                            OutlinedTextField(
                                value = input,
                                onValueChange = { input = it },
                                placeholder = { Text(question.hint) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )

                            Button(
                                onClick = {
                                    if (input.isNotBlank()) {
                                        answers.add(input)
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF834D1E),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("다음")
                            }
                        }
                    }
                }
            }
        } // 대충 질문 받는 칸

        val prompt =
            """
                사용자의 조건은 다음과 같아.
               - 내가 찾으려는 카페는 : ${questionList[0]} 주변에 있어야 해.
               - 오늘의 내 예상 일정은 다음과 같아 : ${questionList[1]}
               - 오늘 날씨는 다음과 같아 : ${questionList[2]}
               - 오늘 나는 ${questionList[4]} 와 함께 카페를 갈 예정이야.
               - 기타 참고할 만한 상황은 다음과 같아 : ${questionList[5]}
               
               조건에 맞는 카페 상위 15개를 추천해 줘.
               **답변은 무조건 네이버 지도 상에 실제 존재하는 카페 상호명을 기준으로,
               "이름1, 이름2, 이름3, 이름4, ..." 와 같은 형식으로 출력해 줘.**
            """

        LaunchedEffect(Unit) {
            promptResult = RetrofitClient.apiService.recommendCafes(PromptRequest(prompt))
        }
        val recommendedCafes = promptResult!!
        recommendedCafes.forEach { cafe ->
            // 대충 탭 안에다가 추천한 카페 리스트 띄우면 될 것 같음. 어떻게 할지는 모르겠음
        }
    }
}


@Composable
fun HotNowScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 상단
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF834D1E)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("이번 주, 가장 사랑받은 카페 🤍", color = Color.White, fontSize = 12.sp)
                    Text(
                        "Iced Coffee\nSweet Heaven",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text("더 알아보기 ➔", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                }
                Image(
                    painter = painterResource(id = R.drawable.img_cafe_sample1),
                    contentDescription = "Iced Coffee",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 랭크
        Text(
            "요즘 사람들이 찾는 카페는?",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            recommendationList.forEach { item ->
                RecommendationCard(item)
            }
        }

        // 샵 이미지
        Text(
            "이런 공간, 요즘 좋아요",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_cafe_sample3),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Text(
                text = "카페 로제",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            )
        }


        // Info 카드
        Text(
            "요즘 뜨는 카페 리스트",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            trendingCafeLists.forEach { item ->
                CafeListCard(
                    title = item.title,
                    username = item.username,
                    imageRes = item.imageRes
                )
            }

        }
    }
}


@Composable
fun CafeListCard(title: String, username: String, imageRes: Int) {
    Box(
        modifier = Modifier
            .padding(end = 12.dp)
            .width(160.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // 배경 이미지
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 그라데이션
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x885C3B1A)
                        )
                    )
                )
        )

        // 글씨
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFDF2E9) // 베이지
            )
            Text(
                text = username,
                fontSize = 12.sp,
                color = Color(0xFFFDF2E9)
            )
        }
    }
}
