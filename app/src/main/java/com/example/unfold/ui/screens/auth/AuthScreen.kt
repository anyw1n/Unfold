package com.example.unfold.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unfold.R

data class AuthUiState(
    val loading: Boolean = false,
    val userLoggedIn: Boolean = false,
    val error: String? = null,
)

@Composable
fun AuthScreen(
    showSnackbar: (String) -> Unit,
    complete: () -> Unit,
) {
    val viewModel = hiltViewModel<AuthViewModel>()
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.userLoggedIn) {
        if (uiState.userLoggedIn) complete()
    }
    LaunchedEffect(uiState) {
        uiState.error?.let { showSnackbar(it) }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .run { if (uiState.loading) blur(10.dp) else this },
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_full),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.padding(horizontal = 32.dp),
                )
            }
            if (uiState.error != null) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .padding(top = 92.dp),
                    colors = if (isSystemInDarkTheme()) {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black,
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                        )
                    },
                    shape = RoundedCornerShape(6.dp),
                    onClick = viewModel::loadToken,
                ) {
                    Text(
                        text = stringResource(R.string.retry),
                        fontWeight = FontWeight.Black,
                    )
                }
            }
        }
        if (uiState.loading) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Black.copy(alpha = 0.3f),
            ) {}
            CircularProgressIndicator()
        }
    }
}
