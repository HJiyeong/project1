package com.example.project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.project1.R
import com.example.project1.model.CafeInfo
import com.example.project1.model.CafeList
import com.example.project1.network.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun CafeListTopBar(navController: NavHostController, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.beige))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CafePlayListScreen(
    navController: NavHostController,
    cid: Int
) {
    var selectedList by remember { mutableStateOf<CafeList?>(null) }
    LaunchedEffect(Unit) {
        selectedList = RetrofitClient.apiService.getCafeListById(cid)
    }
    val cafeList = selectedList!!

    Scaffold(
        topBar = { CafeListTopBar(navController, cafeList.name) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(colorResource(R.color.beige))
                .padding(innerPadding)
                .padding(24.dp),
        ) {
            AsyncImage(
                model = cafeList.imageURL,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Text(
                text = cafeList.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${cafeList.list.size}개의 카페",
                modifier = Modifier.padding(horizontal = 20.dp),
                fontSize = 16.sp,
                color = colorResource(R.color.gray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                cafeList.list.forEach { cafe ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .clickable {
                                navController.navigate("cafeDetail/${cafe.cid}")
                            },
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        AsyncImage(
                            model = cafe.imageURL,
                            contentDescription = null,
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.width(12.dp))
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
        }
    }
}