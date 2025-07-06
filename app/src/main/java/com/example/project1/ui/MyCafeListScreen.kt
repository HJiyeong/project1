package com.example.project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.material.icons.filled.MoreVert
import com.example.project1.data.UserManager

@Composable
fun MyCafeListScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val user = UserManager.currentUser ?: return
    var selectedTab by remember { mutableStateOf("내 카페리스트") }
    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ){ innerPadding ->
        var expandedItemId by remember { mutableStateOf<Int?>(null) }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(colorResource(R.color.beige))
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = "내 카페리스트 페이지", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1.0f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            user.cafeLists.forEach{ cafeList ->

                val isExpanded = expandedItemId == cafeList.cid
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { /* TODO: 상세 페이지 이동 or 수정 */ },
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
                            text = "${cafeList.list?.size?: 0}개의 카페",
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
                                text = { Text("수정") },
                                onClick = {
                                    expandedItemId = cafeList.cid
                                    // TODO: 수정 처리
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("이름 변경") },
                                onClick = {
                                    expandedItemId = cafeList.cid
                                    // TODO: 이름 변경 처리
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("삭제") },
                                onClick = {
                                    expandedItemId = cafeList.cid
                                    // TODO: 삭제 처리
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
            ){
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
                                navController.navigate("cafeinfo/${cafeInfo.cid}")
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
                        Text(text = cafeInfo.shortAddress, fontSize = 12.sp, color = colorResource(R.color.gray))
                    }
                }
            }
        }
    }
}
