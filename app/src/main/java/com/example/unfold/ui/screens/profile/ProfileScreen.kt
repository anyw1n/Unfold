package com.example.unfold.ui.screens.profile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.example.unfold.R
import com.example.unfold.data.models.User
import com.example.unfold.ui.common.ErrorLoadState
import com.example.unfold.ui.common.LoadingLoadState
import com.example.unfold.ui.common.PhotoItem
import com.example.unfold.ui.theme.Palette

data class ProfileUiState(
    val loading: Boolean = false,
    val user: User? = null,
    val likedPhotoIndex: Int? = null,
    val error: String? = null,
)

@Composable
fun ProfileScreen(
    showSnackbar: (String) -> Unit,
    onPhotoClick: (String) -> Unit,
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.error) {
        if (uiState.error == null) return@LaunchedEffect
        showSnackbar(uiState.error)
        viewModel.errorShown()
    }

    if (uiState.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val user = uiState.user ?: return
    val photos = viewModel.likedPhotosFlow.collectAsLazyPagingItems()

    LaunchedEffect(uiState.likedPhotoIndex) {
        if (uiState.likedPhotoIndex == null) return@LaunchedEffect
        photos[uiState.likedPhotoIndex]?.apply {
            likedByUser = !likedByUser
            likes = if (likedByUser) likes.inc() else likes.dec()
        }
        viewModel.photoLiked()
    }

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
            ) {
                Text(
                    text = user.totalPhotos.toString() + stringResource(R.string.x_photos),
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.Black else Palette.Deselected,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = user.totalLikes.toString() + stringResource(R.string.x_liked),
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = user.totalCollections.toString() + stringResource(R.string.x_collections),
                    textAlign = TextAlign.Center,
                    color = if (isSystemInDarkTheme()) Color.Black else Palette.Deselected,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        items(
            count = photos.itemCount,
            key = photos.itemKey(key = { it.id }),
            contentType = photos.itemContentType(),
        ) { index ->
            photos[index]?.let {
                PhotoItem(it, { viewModel.like(it, index) }, onPhotoClick)
            }
        }

        when (val state = photos.loadState.refresh) {
            is LoadState.Error -> item { ErrorLoadState(state.error, photos::retry) }
            is LoadState.Loading -> item { LoadingLoadState() }
            is LoadState.NotLoading -> if (photos.itemCount == 0) {
                item {
                    Text(
                        text = stringResource(R.string.no_items_found),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                    )
                }
            }
        }

        when (val state = photos.loadState.append) {
            is LoadState.Error -> item { ErrorLoadState(state.error, photos::retry) }
            is LoadState.Loading -> item { LoadingLoadState() }
            else -> Unit
        }
    }
}
