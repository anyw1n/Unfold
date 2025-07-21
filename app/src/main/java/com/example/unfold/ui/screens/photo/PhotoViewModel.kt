package com.example.unfold.ui.screens.photo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfold.data.Api
import com.example.unfold.ui.common.RibbonRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val api: Api,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(PhotoUiState())
        private set

    private val id: String = savedStateHandle[RibbonRoutes.PhotoId]!!

    init {
        loadPhoto()
    }

    private fun loadPhoto() = viewModelScope.launch {
        uiState = uiState.copy(loading = true)
        uiState = runCatching { api.getPhoto(id) }.fold(
            onSuccess = { uiState.copy(loading = false, photo = it) },
            onFailure = { uiState.copy(loading = false, error = it.localizedMessage ?: "Error") },
        )
    }

    fun like() = viewModelScope.launch {
        val photo = uiState.photo ?: return@launch
        val wasLiked = !photo.likedByUser
        uiState = runCatching {
            if (wasLiked) api.likePhoto(photo.id) else api.unlikePhoto(photo.id)
        }.fold(
            onSuccess = {
                uiState.copy(
                    photo = photo.copy(
                        likedByUser = wasLiked,
                        likes = if (wasLiked) photo.likes.inc() else photo.likes.dec(),
                    ),
                )
            },
            onFailure = { uiState.copy(error = it.localizedMessage ?: "Error") },
        )
    }

    fun download() = viewModelScope.launch {
        val url = uiState.photo?.urls?.raw ?: return@launch
        uiState = runCatching { api.downloadPhoto(id) }.fold(
            onSuccess = { uiState.copy(download = url) },
            onFailure = { uiState.copy(error = it.localizedMessage ?: "Error") },
        )
    }

    fun downloaded() { uiState = uiState.copy(download = null) }

    fun errorShown() { uiState = uiState.copy(error = null) }
}
