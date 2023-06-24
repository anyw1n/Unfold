package com.example.unfold.ui.screens.onboarding

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.example.unfold.util.OnboardingCompleteKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: SharedPreferences,
) : ViewModel() {

    var uiState by mutableStateOf(OnboardingUiState(OnboardingStep.First))
        private set

    fun next() {
        if (uiState.step.ordinal == OnboardingStep.values().last().ordinal) {
            complete()
        } else {
            uiState = uiState.copy(step = OnboardingStep.values()[uiState.step.ordinal + 1])
        }
    }

    fun previous() {
        uiState = uiState.copy(step = OnboardingStep.values()[uiState.step.ordinal - 1])
    }

    private fun complete() {
        prefs.edit {
            putBoolean(OnboardingCompleteKey, true)
        }
        uiState = uiState.copy(complete = true)
    }
}
