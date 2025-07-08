package com.example.project1.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project1.R
import com.example.project1.model.User
import com.example.project1.network.RetrofitClient
import com.example.project1.utils.getToken

@Composable
fun Profile(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val token = getToken(LocalContext.current)
    var loginUser by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(Unit) {
        loginUser = RetrofitClient.apiService.getCurrentUser(token)
    }
    if (loginUser == null) return
    val user = loginUser!!

    var selectedTab by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopTabs(navController = navController) {}
        },
        bottomBar = {
            BottomTabs(navController = navController, selectedTab) { selectedTab = it }
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.beige))
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
