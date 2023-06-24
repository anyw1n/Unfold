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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.unfold.data.models.Photo
import com.example.unfold.data.models.photoMock
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.ThemedPreview

@Composable
fun PhotoItem(photo: Photo, onTap: ((String) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onTap != null) { onTap?.invoke(photo.id) },
        contentAlignment = Alignment.BottomCenter,
    ) {
        AsyncImage(
            model = photo.urls.regular,
            contentDescription = null,
            placeholder = photo.blurBitmap?.asImageBitmap()?.let { BitmapPainter(it) },
            contentScale = ContentScale.FillWidth,
        )
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
            )
        }
    }
}

@ThemedPreview
@Composable
fun PhotoItemPreview() {
    UnfoldTheme {
        Surface(color = Color.Gray) {
            PhotoItem(photoMock) {}
        }
    }
}
