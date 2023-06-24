package com.example.unfold.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.unfold.R
import com.example.unfold.ui.MainDestinations
import com.example.unfold.ui.theme.Palette

@Composable
fun AppBar(navController: NavController, title: String) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return

    if (!currentRoute.contains(MainDestinations.HomeRoute)) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp),
        color = Palette.Green,
        contentColor = Color.Black,
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
            if (currentRoute == HomeTabs.Ribbon.route) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                )
            } else if (currentRoute == HomeTabs.Profile.route) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(R.drawable.logout),
                    contentDescription = null,
                )
            }
        }
    }
}
