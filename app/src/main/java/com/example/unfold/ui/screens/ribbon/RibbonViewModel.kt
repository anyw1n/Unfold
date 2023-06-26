package com.example.unfold.ui.screens.ribbon

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
class RibbonViewModel @Inject constructor(
    private val api: Api,
    repository: PhotoRepository,
) : ViewModel() {

    var uiState by mutableStateOf(RibbonUiState())
        private set

    val photosFlow = repository.photosFlow.cachedIn(viewModelScope)

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
