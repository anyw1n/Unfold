package com.example.unfold.ui.screens.ribbon

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
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
import com.example.unfold.R
import com.example.unfold.ui.common.ErrorLoadState
import com.example.unfold.ui.common.LoadingLoadState
import com.example.unfold.ui.common.PhotoItem

data class RibbonUiState(
    val likedPhotoIndex: Int? = null,
    val error: String? = null,
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RibbonScreen(showSnackbar: (String) -> Unit, onPhotoClick: (String) -> Unit) {
    val viewModel = hiltViewModel<RibbonViewModel>()
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

    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        columns = StaggeredGridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalItemSpacing = 10.dp,
    ) {
        items(
            count = photos.itemCount,
            key = photos.itemKey(key = { it.id }),
            contentType = photos.itemContentType(),
            span = {
                if (it == 0) StaggeredGridItemSpan.FullLine else StaggeredGridItemSpan.SingleLane
            },
        ) { index ->
            photos[index]?.let {
                if (index == 0) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.top_today),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(top = 21.dp, bottom = 24.dp),
                        )
                        PhotoItem(it, { viewModel.like(it, index) }, onPhotoClick)
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                } else {
                    PhotoItem(it, { viewModel.like(it, index) }, onPhotoClick)
                }
            }
        }

        when (val state = photos.loadState.refresh) {
            is LoadState.Error -> item(span = StaggeredGridItemSpan.FullLine) {
                ErrorLoadState(state.error, photos::retry)
            }
            is LoadState.Loading -> item(span = StaggeredGridItemSpan.FullLine) {
                LoadingLoadState()
            }
            is LoadState.NotLoading -> if (photos.itemCount == 0) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = stringResource(R.string.no_items_found),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                    )
                }
            }
        }

        when (val state = photos.loadState.append) {
            is LoadState.Error -> item(span = StaggeredGridItemSpan.FullLine) {
                ErrorLoadState(state.error, photos::retry)
            }
            is LoadState.Loading -> item(span = StaggeredGridItemSpan.FullLine) {
                LoadingLoadState()
            }
            else -> Unit
        }
    }
}
