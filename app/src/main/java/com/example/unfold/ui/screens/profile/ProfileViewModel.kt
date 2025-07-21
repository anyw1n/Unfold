package com.example.unfold.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unfold.data.Api
import com.example.unfold.data.PhotoRepository
import com.example.unfold.data.models.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val api: Api,
    private val repository: PhotoRepository,
) : ViewModel() {

    var uiState by mutableStateOf(ProfileUiState())
        private set

    lateinit var likedPhotosFlow: Flow<PagingData<Photo>>

    init {
        loadMe()
    }

    private fun loadMe() = viewModelScope.launch {
        uiState = uiState.copy(loading = true)
        uiState = runCatching { api.getMe() }.fold(
            onSuccess = {
                likedPhotosFlow = repository.likedFlow(it.username).cachedIn(viewModelScope)
                uiState.copy(loading = false, user = it)
            },
            onFailure = { uiState.copy(loading = false, error = it.localizedMessage ?: "Error") },
        )
    }

    fun like(photo: Photo, index: Int) = viewModelScope.launch {
        val wasLiked = !photo.likedByUser
        uiState = runCatching {
            if (wasLiked) api.likePhoto(photo.id) else api.unlikePhoto(photo.id)
        }.fold(
            onSuccess = {
                uiState.copy(
                    likedPhotoIndex = index,
                    user = uiState.user?.copy(
                        totalLikes = if (wasLiked) {
                            uiState.user?.totalLikes?.inc()
                        } else {
                            uiState.user?.totalLikes?.dec()
                        },
                    ),
                )
            },
            onFailure = { uiState.copy(error = it.localizedMessage ?: "Error") },
        )
    }

    fun photoLiked() { uiState = uiState.copy(likedPhotoIndex = null) }

    fun errorShown() { uiState = uiState.copy(error = null) }
}
