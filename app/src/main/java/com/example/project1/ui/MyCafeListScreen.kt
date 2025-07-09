package com.example.project1.ui

import android.R.bool
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.CafeInfo
import com.example.project1.model.CafeList
import com.example.project1.model.User
import coil3.compose.AsyncImage

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.project1.model.ListChangeNameRequest
import com.example.project1.model.ListCreateRequest
import com.example.project1.network.RetrofitClient
import com.example.project1.utils.getToken
import kotlinx.coroutines.launch
import retrofit2.Retrofit

@Composable
fun Dialog(
    navController: NavHostController,
    onDismiss: () -> Unit, id: Int, isCreate: Boolean
) {
    var input by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.tab).copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp)
                .width(300.dp)
        ) {
            val upperText = if (isCreate) "새 카페 리스트 만들기" else "이름 변경"
            Text(upperText, fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { if (it.length <= 40) input = it },
                placeholder = { Text("카페 리스트 이름 입력", color = colorResource(R.color.brown)) },
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorResource(R.color.brown), cursorColor = colorResource(R.color.brown), focusedTextColor = colorResource(R.color.brown), unfocusedBorderColor = colorResource(R.color.brown), unfocusedTextColor = colorResource(R.color.brown)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.beige))
                ) {
                    Text("취소", color = colorResource(R.color.brown))
                }
                val coroutineScope = rememberCoroutineScope()
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (isCreate) {
                                val newCafeList = RetrofitClient.apiService.createCafeList(
                                    ListCreateRequest(name = input, uid = id)
                                )
                                RetrofitClient.apiService.setDefaultImage(newCafeList.cid)
                                navController.navigate("addcafetoplaylist/${newCafeList.cid}")
                            } else {
                                RetrofitClient.apiService.changeListName(
                                    ListChangeNameRequest(name = input, cid = id)
                                )
                                onDismiss
                                navController.navigate("mylist")
                            }
                        }
                    },
                    enabled = input.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor =
                        if (input.isNotBlank()) colorResource(R.color.brown) else colorResource(R.color.beige))
                ) {
                    val buttonText = if (isCreate) "생성" else "이름 변경"
                    Text(buttonText, color =
                        if (input.isNotBlank()) Color.White else colorResource(R.color.brown))
                }
            }
        }
    }
}

@Composable
fun MyCafeListScreen(
    navController: NavHostController,
) {
    val token = getToken(LocalContext.current)
    var loginUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(Unit) {
        loginUser = RetrofitClient.apiService.getCurrentUser(token)
    }
    if (loginUser == null) return
    val user = loginUser!!

    var selectedTab by remember { mutableStateOf("내 카페리스트") }
    var expanded by remember { mutableStateOf(false) }
    var createNewList by remember { mutableStateOf(false) }
    var changeName by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ){ innerPadding ->
        if (user.recommendation.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.beige))
                    .padding(innerPadding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(75.dp))

                // 이미지 추가?
                Text(text = "아직 팔로우한 카페가 없어요.",
                    fontSize = 18.sp,
                    color = colorResource(R.color.brown)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "다양한 카페를 추가하고, 나만의 카페 플레이리스트를 만들어보세요!",
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { navController.navigate("curation") },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.brown))
                ) { Text("카페 찾아보기") }
            }
        } else {
            var expandedItemId by remember { mutableStateOf<Int?>(null) }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(colorResource(R.color.beige))
                        .padding(innerPadding)
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { expanded = !expanded } ) {
                            Text(text = "내 카페리스트 페이지", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (expanded) "▾" else "▸", fontSize = 20.sp)
                        }

                        IconButton(onClick = {
                            createNewList = true
                        }) {
                            Icon(Icons.Default.Add, contentDescription = "추가")
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    user.cafeLists.forEach { cafeList ->
                        val coroutineScope = rememberCoroutineScope()
                        val isExpanded = expandedItemId == cafeList.cid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { navController.navigate("cafePlayList/${cafeList.cid}") },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = cafeList.imageURL,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1.0f)) {
                                Text(text = cafeList.name, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = "${cafeList.list?.size ?: 0}개의 카페",
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.gray)
                                )
                            }
                            Box {
                                IconButton(onClick = {
                                    expandedItemId = if (isExpanded) null else cafeList.cid
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.MoreVert,
                                        contentDescription = "More options"
                                    )
                                }
                                DropdownMenu(
                                    expanded = isExpanded,
                                    onDismissRequest = { expandedItemId = null }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("이름 변경") },
                                        onClick = {
                                            expandedItemId = cafeList.cid
                                            changeName = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("삭제") },
                                        onClick = {
                                            coroutineScope.launch {
                                                RetrofitClient.apiService.deleteList(cafeList.cid)
                                            }
                                            navController.navigate("mylist")
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "내가 구독한 카페", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    user.recommendation.forEach { cafeInfo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable(
                                    onClick = {
                                        navController.navigate("cafeDetail/${cafeInfo.cid}")
                                    }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = cafeInfo.imageURL,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(text = cafeInfo.name, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = cafeInfo.shortAddress,
                                    fontSize = 12.sp,
                                    color = colorResource(R.color.gray)
                                )
                            }
                        }
                    }
                }
                if (createNewList) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        awaitPointerEvent()
                                    }
                                }
                            }
                    )
                    Dialog(
                        navController = navController,
                        onDismiss = { createNewList = false },
                        id = user.uid,
                        isCreate = true
                    )
                }
                if (changeName) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        awaitPointerEvent()
                                    }
                                }
                            }
                    )
                    Dialog(
                        navController = navController,
                        onDismiss = { changeName = false },
                        id = user.uid,
                        isCreate = false
                    )
                }
            }
        }
    }
}
