package com.example.unfold.ui.screens.collections

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.data.models.collectionMock
import com.example.unfold.ui.common.CollectionItem

@Composable
fun CollectionsScreen(collectionTap: (String) -> Unit) {
    val viewModel = hiltViewModel<CollectionsViewModel>()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(List(10) { collectionMock }) {
            CollectionItem(it, collectionTap)
        }
    }
}
