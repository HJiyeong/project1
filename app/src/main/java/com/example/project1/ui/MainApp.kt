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
import com.example.project1.model.CafeInfo
import com.example.project1.network.RetrofitClient
import androidx.compose.runtime.getValue

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LogInScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("followers") { FollowerScreen(navController) }
        composable("curation") { CurationScreen(navController) }
        composable("mylist") { MyCafeListScreen(navController) }
        composable("setting") { Setting(navController) }
        composable("info") { Profile(navController) }
        composable("add_follower") { AddFollower(navController) }

        composable("cafeDetail/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid")!!.toInt()
            var cafe by remember { mutableStateOf<CafeInfo?>(null) }

            LaunchedEffect(cid) {
                cafe = RetrofitClient.apiService.getCafeById(cid)
            }

            cafe?.let {
                CafeDetailScreen(navController, it)
            }
        }

        composable("addcafetoplaylist/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid")!!.toInt()
            AddCafeToListScreen(navController, cid) }
        composable("cafePlayList/{cid}") { backStackEntry ->
            val cid = backStackEntry.arguments?.getString("cid")!!.toInt()
            CafePlayListScreen(navController, cid)
        }
    }
}
