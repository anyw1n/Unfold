package com.example.unfold.ui.screens.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.unfold.R
import com.example.unfold.data.models.collectionMock
import com.example.unfold.data.models.photoMock
import com.example.unfold.ui.common.LikesItem
import com.example.unfold.ui.common.UserItem

@Composable
fun CollectionScreen(setTitle: (String) -> Unit) {
    val viewModel = hiltViewModel<CollectionViewModel>()
    val collection = collectionMock

    LaunchedEffect(Unit) { setTitle(collection.title) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.White),
            ) {
                AsyncImage(
                    model = collection.coverPhoto.urls.regular,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f,
                )
                Column(
                    modifier = Modifier.padding(top = 14.dp, end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = collection.title,
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = collection.description ?: "",
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Text(
                        text = "${collection.totalPhotos} images by @${collection.user.username}",
                        fontSize = 10.sp,
                        color = Color.Black,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 2.dp),
                    )
                }
            }
        }
        items(List(10) { photoMock }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                AsyncImage(
                    model = it.urls.regular,
                    contentDescription = null,
                    placeholder = it.blurBitmap?.asImageBitmap()?.let { BitmapPainter(it) },
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp, end = 4.dp, bottom = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    UserItem(user = it.user)
                    Spacer(modifier = Modifier.weight(1f))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Download",
                            textDecoration = TextDecoration.Underline,
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
                        )
                        Text(
                            text = " (${it.downloads})",
                            color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.download),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.background else Color.White,
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    LikesItem(
                        likesCount = it.likes,
                        liked = it.likedByUser,
                    )
                }
            }
        }
    }
}
