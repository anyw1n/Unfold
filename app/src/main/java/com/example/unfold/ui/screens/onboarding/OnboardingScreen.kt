package com.example.unfold.ui.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.R

enum class OnboardingStep(
    @StringRes val textId: Int,
    val hasPrev: Boolean,
) {
    First(R.string.onboarding_first, false),
    Second(R.string.onboarding_second, true),
    Third(R.string.onboarding_third, true),
}

data class OnboardingUiState(
    val step: OnboardingStep,
    val complete: Boolean = false,
)

@Composable
fun OnboardingScreen(complete: () -> Unit) {
    val viewModel = hiltViewModel<OnboardingViewModel>()
    val uiState = viewModel.uiState

    BackHandler(uiState.step.hasPrev, viewModel::previous)
    LaunchedEffect(uiState.complete) {
        if (uiState.complete) complete()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp)
                .align(Alignment.TopCenter),
        ) {
            if (uiState.step.hasPrev) {
                IconButton(onClick = viewModel::previous) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = viewModel::next) {
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                )
            }
        }
        Image(
            painter = painterResource(id = R.drawable.onboard_image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        ) {
            Image(
                painter = painterResource(id = R.drawable.gradient),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(id = uiState.step.textId),
                fontSize = 27.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(Alignment.CenterStart),
            )
        }
    }
}
