package com.example.project1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.project1.R

@Composable // FIN
fun BottomTabs(navController: NavController, selected: String, onSelect: (String) -> Unit) {
    val tabs = listOf(
        Triple("Home", "home", painterResource(R.drawable.bottomtab_home)),
        Triple("팔로워", "followers", painterResource(R.drawable.ic_3)),
        Triple("카페 큐레이션", "curation", painterResource(R.drawable.ic_info)),
        Triple("내 카페리스트", "mylist", painterResource(R.drawable.ic_4))
    )
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(colorResource(R.color.brown))
            .padding(vertical=8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { (tab, route, image) ->
            val isSelected = tab == selected
            Box(
                modifier = Modifier
                    .background(colorResource(R.color.brown))
                    .clickable { onSelect(tab) }
                    .shadow(elevation = if (isSelected) 4.dp else 0.dp, shape = RoundedCornerShape(32.dp))
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(colorResource(R.color.brown))
                        .clickable {
                            onSelect(tab)
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = image,
                        contentDescription = tab,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = tab,
                        color = colorResource(R.color.white),
                        fontSize = if (tab.length > 5) 10.sp else 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
