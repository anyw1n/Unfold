package com.example.unfold.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.unfold.ui.MainDestinations
import com.example.unfold.ui.theme.Palette

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: return

    if (!currentRoute.contains(MainDestinations.HomeRoute)) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(76.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val selectedColor = if (isSystemInDarkTheme()) Palette.SelectedDark else Color.Black
            val deselectedColor = if (isSystemInDarkTheme()) Color.Black else Palette.Deselected

            HomeTabs.values().forEach {
                IconButton(onClick = {
                    if (!currentRoute.contains(it.route)) {
                        navController.navigate(it.route) {
                            popUpTo(MainDestinations.HomeRoute) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = null,
                        tint = if (currentRoute.contains(it.route)) selectedColor else deselectedColor,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}
