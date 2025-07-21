package com.example.unfold.ui.screens.collection

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unfold.data.Api
import com.example.unfold.data.PhotoRepository
import com.example.unfold.data.models.Photo
import com.example.unfold.ui.common.CollectionsRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val api: Api,
    savedStateHandle: SavedStateHandle,
    repository: PhotoRepository,
) : ViewModel() {
    var uiState by mutableStateOf(CollectionUiState())
        private set

    private val id: String = savedStateHandle[CollectionsRoutes.CollectionId]!!

    val photosFlow = repository.collectionPhotosFlow(id).cachedIn(viewModelScope)

    init {
        loadCollection()
    }

    private fun loadCollection() = viewModelScope.launch {
        uiState = uiState.copy(loading = true)
        uiState = runCatching { api.getCollection(id) }.fold(
            onSuccess = { uiState.copy(loading = false, collection = it) },
            onFailure = { uiState.copy(loading = false, error = it.localizedMessage ?: "Error") },
        )
    }

    fun like(photo: Photo, index: Int) = viewModelScope.launch {
        val wasLiked = !photo.likedByUser
        uiState = runCatching {
            if (wasLiked) api.likePhoto(photo.id) else api.unlikePhoto(photo.id)
        }.fold(
            onSuccess = { uiState.copy(likedPhotoIndex = index) },
            onFailure = { uiState.copy(error = it.localizedMessage ?: "Error") },
        )
    }

    fun photoLiked() { uiState = uiState.copy(likedPhotoIndex = null) }

    fun errorShown() { uiState = uiState.copy(error = null) }
}
