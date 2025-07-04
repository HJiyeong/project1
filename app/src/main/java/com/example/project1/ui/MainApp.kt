package com.example.project1.ui  // ← 너희 프로젝트 패키지에 맞게 수정

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController

import com.example.project1.ui.HomeScreen
import com.example.project1.ui.FollowerScreen
import com.example.project1.ui.CurationScreen
import com.example.project1.ui.MyCafeListScreen

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("followers") { FollowerScreen(navController) }
        composable("curation") { CurationScreen(navController) }
        composable("mylist") { MyCafeListScreen(navController) }
        composable("setting") { Setting(navController) }
        composable("info") { Profile(navController) }
    }
}
