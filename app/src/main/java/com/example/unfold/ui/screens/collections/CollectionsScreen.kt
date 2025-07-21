package com.example.unfold.ui.screens.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.unfold.R
import com.example.unfold.ui.common.CollectionItem
import com.example.unfold.ui.common.ErrorLoadState
import com.example.unfold.ui.common.LoadingLoadState

@Composable
fun CollectionsScreen(onCollectionClick: (String) -> Unit) {
    val viewModel = hiltViewModel<CollectionsViewModel>()
    val collections = viewModel.collectionsFlow.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(
            count = collections.itemCount,
            key = collections.itemKey(key = { it.id }),
            contentType = collections.itemContentType(),
        ) { index ->
            collections[index]?.let {
                CollectionItem(it, onCollectionClick)
            }
        }

        when (val state = collections.loadState.refresh) {
            is LoadState.Error -> item { ErrorLoadState(state.error, collections::retry) }
            is LoadState.Loading -> item { LoadingLoadState() }
            is LoadState.NotLoading -> if (collections.itemCount == 0) {
                item {
                    Text(
                        text = stringResource(R.string.no_items_found),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(all = 16.dp),
                    )
                }
            }
        }

        when (val state = collections.loadState.append) {
            is LoadState.Error -> item { ErrorLoadState(state.error, collections::retry) }
            is LoadState.Loading -> item { LoadingLoadState() }
            else -> Unit
        }
    }
}
