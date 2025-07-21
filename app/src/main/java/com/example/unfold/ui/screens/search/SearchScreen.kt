package com.example.unfold.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.example.unfold.R
import com.example.unfold.ui.common.ErrorLoadState
import com.example.unfold.ui.common.LoadingLoadState
import com.example.unfold.ui.common.PhotoItem
import com.example.unfold.ui.theme.Palette

data class SearchUiState(
    val query: String? = null,
    val likedPhotoIndex: Int? = null,
    val error: String? = null,
)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    showSnackbar: (String) -> Unit,
    onPhotoClick: (String) -> Unit,
) {
    val viewModel = hiltViewModel<SearchViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.error) {
        if (uiState.error == null) return@LaunchedEffect
        showSnackbar(uiState.error)
        viewModel.errorShown()
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier.fillMaxWidth().height(46.dp),
            color = Palette.Green,
            contentColor = Color.Black,
            shadowElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                var query by remember { mutableStateOf("") }
                val keyboardController = LocalSoftwareKeyboardController.current
                val focusManager = LocalFocusManager.current

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = null,
                        )
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp).clickable {
                                query = ""
                                viewModel.search(null)
                            },
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        viewModel.search(query)
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Palette.Green,
                        focusedContainerColor = Palette.Green,
                    ),
                )
            }
        }

        val photos = if (uiState.query != null) viewModel.getPhotos(uiState.query).collectAsLazyPagingItems() else return

        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
}
