package com.example.unfold

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.unfold.data.Api
import com.example.unfold.data.CredentialsRepository
import com.example.unfold.ui.MainDestinations
import com.example.unfold.ui.NavGraph
import com.example.unfold.ui.common.AppBar
import com.example.unfold.ui.common.BottomBar
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.OnboardingCompleteKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var credentialsRepository: CredentialsRepository

    @Inject lateinit var prefs: SharedPreferences

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onboardingComplete = prefs.getBoolean(OnboardingCompleteKey, false)
        val userLoggedIn = credentialsRepository.token != null
        setContent {
            UnfoldTheme {
                val navController = rememberNavController()

                val appName = stringResource(R.string.app_name)
                var title by remember { mutableStateOf(appName) }
                val snackbarState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                Scaffold(
                    topBar = {
                        AppBar(
                            navController = navController,
                            title = title,
                        )
                    },
                    bottomBar = {
                        BottomBar(navController = navController)
                    },
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarState) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                color = Color.Black.copy(alpha = 0.5f),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = it.visuals.message,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White,
                                    )
                                    IconButton(onClick = it::dismiss) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Color.White,
                                        )
                                    }
                                }
                            }
                        }
                    },
                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        startDestination = when {
                            !onboardingComplete -> MainDestinations.OnboardingRoute
                            userLoggedIn -> MainDestinations.HomeRoute
                            else -> MainDestinations.LoginRoute
                        },
                        paddingValues = paddingValues,
                        login = { startActivity(Intent(Intent.ACTION_VIEW, Api.OAuthUri)) },
                        setTitle = { title = it },
                        showSnackbar = { coroutineScope.launch { snackbarState.showSnackbar(it) } },
                    )
                }
            }
        }
    }
}
