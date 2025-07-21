package com.example.unfold.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unfold.R
import com.example.unfold.ui.theme.Palette
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.ThemedPreview

@Composable
fun LikesItem(likesCount: Int, liked: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = likesCount.toString(),
            fontSize = 11.sp,
            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
        )
        Icon(
            painter = if (liked) {
                painterResource(id = R.drawable.favorite_filled)
            } else {
                painterResource(
                    id = R.drawable.favorite_outlined,
                )
            },
            contentDescription = null,
            tint = if (liked) Palette.HeartRed else if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
            modifier = Modifier
                .size(20.dp)
                .padding(start = 4.dp)
                .clickable { onClick() },
        )
    }
}

@ThemedPreview
@Composable
fun LikesItemPreview() {
    UnfoldTheme {
        Surface(color = Color.Gray) {
            Column {
                LikesItem(123, false) {}
                LikesItem(13, true) {}
            }
        }
    }
}
