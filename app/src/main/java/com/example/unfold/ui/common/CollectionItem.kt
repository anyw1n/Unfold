package com.example.unfold.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unfold.R
import com.example.unfold.data.models.Collection
import com.example.unfold.data.models.collectionMock
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.ThemedPreview

@Composable
fun CollectionItem(collection: Collection, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(195.dp)
            .clickable { onClick(collection.id) },
    ) {
        AsyncPhoto(
            model = collection.coverPhoto,
            contentScale = ContentScale.Crop,
            saveAspectRatio = false,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 19.dp, top = 12.dp, bottom = 8.dp),
        ) {
            Text(
                text = collection.totalPhotos.toString() + stringResource(R.string.photos),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
            )
            Text(
                text = collection.title.uppercase(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
            )
            Spacer(modifier = Modifier.weight(1f))
            UserItem(user = collection.user)
        }
    }
}

@ThemedPreview
@Composable
fun CollectionItemPreview() {
    UnfoldTheme {
        Surface(color = Color.Gray) {
            CollectionItem(collectionMock) {}
        }
    }
}
