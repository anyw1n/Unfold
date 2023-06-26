package com.example.unfold.ui.screens.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import com.example.unfold.data.models.Collection
import com.example.unfold.ui.common.ErrorLoadState
import com.example.unfold.ui.common.LoadingLoadState
import com.example.unfold.ui.common.PhotoItem

data class CollectionUiState(
    val loading: Boolean = false,
    val collection: Collection? = null,
    val likedPhotoIndex: Int? = null,
    val error: String? = null,
)

@Composable
fun CollectionScreen(
    setTitle: (String) -> Unit,
    showSnackbar: (String) -> Unit,
    onPhotoClick: (String) -> Unit,
) {
    val viewModel = hiltViewModel<CollectionViewModel>()
    val uiState = viewModel.uiState
    val photos = viewModel.photosFlow.collectAsLazyPagingItems()

    LaunchedEffect(uiState.error) {
        if (uiState.error == null) return@LaunchedEffect
        showSnackbar(uiState.error)
        viewModel.errorShown()
    }

    LaunchedEffect(uiState.likedPhotoIndex) {
        if (uiState.likedPhotoIndex == null) return@LaunchedEffect
        photos[uiState.likedPhotoIndex]?.apply {
            likedByUser = !likedByUser
            likes = if (likedByUser) likes.inc() else likes.dec()
        }
        viewModel.photoLiked()
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

    val collection = uiState.collection ?: return

    LaunchedEffect(collection) { setTitle(collection.title.uppercase()) }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Box(modifier = Modifier.height(IntrinsicSize.Min)) {
                AsyncImage(
                    model = collection.coverPhoto.urls.regular,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.4f,
                    modifier = Modifier.background(Color.White),
                )
                Column(
                    modifier = Modifier.fillMaxWidth().padding(top = 14.dp, end = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = collection.title.uppercase(),
                        fontSize = 14.sp,
                        color = Color.Black,
                    )
                    Text(
                        text = collection.tags.joinToString(" ") { "#${it.title}" },
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = collection.description ?: "",
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        text = "${collection.totalPhotos} images by @${collection.user.username}",
                        fontSize = 10.sp,
                        color = Color.Black,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 2.dp),
                    )
                }
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
