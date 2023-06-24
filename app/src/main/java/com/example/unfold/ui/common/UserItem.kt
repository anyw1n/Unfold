package com.example.unfold.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.unfold.data.models.User
import com.example.unfold.data.models.mockUser
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.ThemedPreview

@Composable
fun UserItem(user: User) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = user.profileImage.large,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape),
        )
        Column(
            modifier = Modifier.padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = user.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
            )
            Text(
                text = "@${user.username}",
                fontSize = 11.sp,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
            )
        }
    }
}

@ThemedPreview
@Composable
fun UserItemPreview() {
    UnfoldTheme {
        Surface(color = Color.Gray) {
            UserItem(mockUser)
        }
    }
}
