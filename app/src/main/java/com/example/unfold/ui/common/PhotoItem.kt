package com.example.unfold.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.unfold.data.models.Photo
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.ThemedPreview

@Composable
fun PhotoItem(photo: Photo, onLike: () -> Unit, onClick: ((String) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke(photo.id) },
        contentAlignment = Alignment.BottomCenter,
    ) {
        AsyncPhoto(model = photo)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 3.dp, end = 4.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserItem(user = photo.user)
            Spacer(modifier = Modifier.weight(1f))
            LikesItem(
                likesCount = photo.likes,
                liked = photo.likedByUser,
                onClick = onLike,
            )
        }
    }
}

@ThemedPreview
@Composable
fun PhotoItemPreview() {
    UnfoldTheme {
        Surface(color = Color.Gray) {
            PhotoItem(Photo.Mock, { })
        }
    }
}
