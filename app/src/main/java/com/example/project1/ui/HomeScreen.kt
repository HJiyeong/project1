package com.example.project1.ui
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.BookmarkBorder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project1.R
import androidx.navigation.NavHostController


data class FortuneCategory(
    val title: String,
    val emoji: String,
    val message: String,
    val cafe: String,
    val imageRes: Int, // 카페 이미지  ID
    val cafeId: String  // 상세 페이지 ID
)



@Composable
fun HomeScreen(navController: NavHostController,
               modifier: Modifier = Modifier,
               userName: String = "지영") {
    var selectedTab by remember { mutableStateOf("Home") }


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
                .background(colorResource(R.color.beige))
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        )
        {
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "오늘의 운세를 확인하고, 나에게 딱 맞는 카페를 만나보세요 🌟",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            StorySection(navController = navController)
            Spacer(modifier = Modifier.height(24.dp))
            FeedList()
        }
    }
}



@Composable
fun StorySection(navController: NavHostController) {
    val fortuneList = listOf(
        FortuneCategory("재물운", "💰", "지출보다 절약에 집중하세요.", "가성비 카페 ‘슬기로운 커피생활’", R.drawable.img_cafe_sample1, "cafe1"),
        FortuneCategory("애정운", "❤️", "마음을 전하기 좋은 날이에요.", "감성 가득 ‘달빛다방’", R.drawable.img_cafe_sample2, "cafe2"),
        FortuneCategory("건강운", "🧘", "허브차로 몸과 마음을 정화하세요.", "허브 힐링카페 ‘쉼표’", R.drawable.img_cafe_sample3, "cafe3"),
        FortuneCategory("행운운", "🍀", "작은 행운이 당신 곁에 있어요.", "행운의 포춘카페 ‘럭키빈’", R.drawable.img_cafe_sample1, "cafe4"),
        FortuneCategory("여행운", "✈️", "바람쐬기 좋은 날, 감성카페로 GO!", "테라스 좋은 ‘바람카페’", R.drawable.img_cafe_sample2, "cafe5"),
        FortuneCategory("집중운", "📚", "오늘의 몰입력 최상! 공부카페 추천", "스터디에 최적 ‘집중다방’", R.drawable.img_cafe_sample3, "cafe6"),
        FortuneCategory("인간관계운", "🤝", "대화가 술술 풀리는 하루예요.", "분위기 좋은 ‘소셜카페’", R.drawable.img_cafe_sample1, "cafe7"),
        FortuneCategory("휴식운", "☕", "혼자만의 여유를 즐기기에 딱 좋아요.", "조용한 ‘무드카페’", R.drawable.img_cafe_sample2, "cafe8")
    )


    var selectedFortune by remember { mutableStateOf<FortuneCategory?>(null) }


    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(fortuneList) { fortune ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { selectedFortune = fortune }
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(fortune.emoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(fortune.title, fontSize = 12.sp)
            }
        }
    }

    selectedFortune?.let {
        FortunePopup(fortune = it, navController = navController) {
            selectedFortune = null
        }
    }
}


@Composable
fun FortunePopup(
    fortune: FortuneCategory,
    navController: NavHostController,  // ✅ NavController 필요
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Row {
                TextButton(onClick = {
                    // ✅ 추후 구현할 상세페이지로 네비게이션
                    navController.navigate("cafe/${fortune.cafeId}")
                    onDismiss()
                }) {
                    Text("자세히 보기")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("닫기")
                }
            }
        },
        title = {
            Text("오늘의 ${fortune.title}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // ✅ 카페 이미지 썸네일
                Image(
                    painter = painterResource(id = fortune.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("${fortune.emoji} ${fortune.message}", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("👉 추천 카페: ${fortune.cafe}", fontWeight = FontWeight.Medium)
            }
        }
    )
}



@Composable
fun FeedList() {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        repeat(5) { index ->
            FeedItem(userName = "user$index", imageRes = R.drawable.img_cafe_sample1)
        }
    }
}
@Composable
fun FeedItem(userName: String, imageRes: Int) {
    val liked = remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(12) }

    var saved by remember { mutableStateOf(false) }
    var saveCount by remember { mutableStateOf(3) }

    val showComments = remember { mutableStateOf(false) }
    val comments = remember { mutableStateListOf("너무 좋아요!", "여기 어디예요?") }
    var commentText by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxWidth()) {

        //이름
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(userName, fontWeight = FontWeight.Bold)
        }

        // 사진
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentScale = ContentScale.Crop
        )

        // 아이콘 (좋아요, 댓글, 저장)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(imageVector = if (liked.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "좋아요",
                    tint = if (liked.value) Color.Red else Color.Black,
                    modifier = Modifier.clickable { liked.value = !liked.value
                        likeCount += if (liked.value) 1 else -1
                    }
                )
                Icon(imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = "댓글",
                    modifier = Modifier.clickable {
                        showComments.value = !showComments.value
                    })
            }
            Icon(
                imageVector = if (saved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = "저장",
                tint = if (saved) Color(0xFF3E64FF) else Color.Black,
                modifier = Modifier.clickable {
                    saved = !saved
                    saveCount += if (saved) 1 else -1
                }
            )

        }

        Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
            Text("❤️ $likeCount 명", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("💬 댓글 ${comments.size}개", fontSize = 13.sp, color = Color.Gray)
            Text("📌 저장 $saveCount 회", fontSize = 13.sp, color = Color.Gray)
        }

        Text(
            text = "$userName 님이 카페를 추가했어요.",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 14.sp
        )

        //댓글창
        if (showComments.value) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                comments.forEach { comment ->
                    Text("💬 $comment", fontSize = 13.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("댓글을 입력하세요...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    trailingIcon = {
                        Text(
                            text = "게시",
                            modifier = Modifier
                                .clickable(enabled = commentText.isNotBlank()) {
                                    comments.add(commentText.trim())
                                    commentText = ""
                                },
                            color = if (commentText.isNotBlank()) Color(0xFF007AFF) else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }
        }
    }
}





