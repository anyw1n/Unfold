package com.example.unfold.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unfold.data.Api
import com.example.unfold.data.PhotoRepository
import com.example.unfold.data.models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val api: Api,
    private val repository: PhotoRepository,
) : ViewModel() {

    var uiState by mutableStateOf(SearchUiState())
        private set

    fun search(query: String?) { uiState = uiState.copy(query = query) }

    fun getPhotos(query: String) = repository.searchFlow(query).cachedIn(viewModelScope)

    fun like(photo: Photo, index: Int) = viewModelScope.launch {
        val wasLiked = !photo.likedByUser
        uiState = runCatching {
            if (wasLiked) api.likePhoto(photo.id) else api.unlikePhoto(photo.id)
        }.fold(
            onSuccess = { uiState.copy(likedPhotoIndex = index) },
            onFailure = { uiState.copy(error = it.localizedMessage ?: "Error") },
        )
    }

    fun errorShown() { uiState = uiState.copy(error = null) }
}
