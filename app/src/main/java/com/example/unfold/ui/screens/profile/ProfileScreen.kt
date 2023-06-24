package com.example.unfold.ui.screens.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.unfold.R
import com.example.unfold.data.models.mockUser
import com.example.unfold.data.models.photoMock
import com.example.unfold.ui.common.LikesItem
import com.example.unfold.ui.common.UserItem
import com.example.unfold.ui.theme.Palette

@Composable
fun ProfileScreen() {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val user = mockUser

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 12.dp, end = 12.dp),
            ) {
                AsyncImage(
                    model = user.profileImage.large,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(69.dp)
                        .clip(CircleShape),
                )
                Column(modifier = Modifier.padding(start = 15.dp, top = 3.dp)) {
                    Text(
                        text = user.name,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) Color.White else Palette.DarkGray,
                    )
                    Text(
                        text = "@${user.username}",
                        fontSize = 14.sp,
                        color = if (isSystemInDarkTheme()) Color.White else Palette.SelectedDark,
                    )
                    Text(
                        text = user.bio ?: "",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                    Row(modifier = Modifier.padding(top = 13.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.location),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = user.location ?: "",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.mail),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = user.email ?: "",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                    Row(modifier = Modifier.padding(top = 4.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.download),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = user.downloads?.toString() ?: "0",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 6.dp),
                        )
                    }
                }
            }
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "${user.totalPhotos}\nфотографии",
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.Black else Palette.Deselected,
                )
                Text(
                    text = "${user.totalLikes}\nпонравилось",
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                )
                Text(
                    text = "${user.totalCollections}\nколлекции",
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.Black else Palette.Deselected,
                )
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
