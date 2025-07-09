package com.example.project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.CafeInfo
import com.example.project1.model.CafeList
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.launch


@Composable
fun AddCafeToListScreen(
    navController: NavHostController,
    cid: Int,
) {
    var candidateList by remember { mutableStateOf<List<CafeInfo>?>(null) }

    LaunchedEffect(Unit) {
        candidateList = RetrofitClient.apiService.getCafeCandidates(cid)
    }

    // candidateList 로딩 중이면 아무것도 안 띄움 (또는 로딩 UI 넣어도 됨)
    val cafeList = candidateList ?: return

    var selectedIds by remember { mutableStateOf(setOf<Int>()) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.beige))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedIds.isNotEmpty()) "${selectedIds.size}개 선택됨" else "카페 추가",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = if (selectedIds.isNotEmpty()) 80.dp else 0.dp)
            ) {
                cafeList.forEach { cafe ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedIds = if (selectedIds.contains(cafe.cid))
                                    selectedIds - cafe.cid
                                else
                                    selectedIds + cafe.cid
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedIds.contains(cafe.cid),
                            onCheckedChange = {
                                selectedIds = if (it)
                                    selectedIds + cafe.cid
                                else
                                    selectedIds - cafe.cid
                            }
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = cafe.name, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = cafe.shortAddress,
                                fontSize = 12.sp,
                                color = colorResource(R.color.gray)
                            )
                        }
                    }
                }
            }

            if (selectedIds.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                selectedIds.forEach { id ->
                                    RetrofitClient.apiService.addCafes(cid, id)
                                }
                                navController.navigate("mylist")
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.brown))
                    ) {
                        Text("확인 (${selectedIds.size}개 추가)", color = Color.White)
                    }
                }
            }
        }
    }
}