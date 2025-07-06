package com.example.project1.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project1.R
import com.example.project1.data.UserManager
import com.example.project1.model.CafeInfo
import com.example.project1.network.RetrofitClient
import androidx.compose.runtime.getValue

@Composable
fun MainApp() {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        val users = RetrofitClient.apiService.getUsers()
        UserManager.currentUser = users.firstOrNull() // 일단은 first dummy user 자동 로그인
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("followers") { FollowerScreen(navController) }
        composable("curation") { CurationScreen(navController) }
        composable("mylist") { MyCafeListScreen(navController) }
        composable("setting") { Setting(navController) }
        composable("info") { Profile(navController) }
        composable("cafeinfo/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getInt("cid")!!
            var cafe by remember { mutableStateOf<CafeInfo?>(null) }

            LaunchedEffect(cid) {
                cafe = RetrofitClient.apiService.getCafeById(cid)
            }
            cafe?.let { cafe ->
                CafeInfo(cafe, navController)
            }
        }
        composable("cafeDetail") {
            CafeDetailScreen(navController)
        }




    }
}
