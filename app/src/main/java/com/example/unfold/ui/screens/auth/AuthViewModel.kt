package com.example.unfold.ui.screens.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unfold.data.Api
import com.example.unfold.data.CredentialsRepository
import com.example.unfold.data.models.TokenBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val api: Api,
    private val credentialsRepository: CredentialsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    private val code: String = savedStateHandle["code"]!!

    init {
        loadToken()
    }

    fun loadToken() = viewModelScope.launch {
        uiState = AuthUiState(loading = true)
        uiState = runCatching { api.getAccessToken(TokenBody(code)) }.fold(
            onSuccess = {
                credentialsRepository.token = it.accessToken
                uiState.copy(userLoggedIn = true)
            },
            onFailure = {
                uiState.copy(
                    loading = false,
                    error = it.localizedMessage ?: "Error",
                )
            },
        )
    }
}
