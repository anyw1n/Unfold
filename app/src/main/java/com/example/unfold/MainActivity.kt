package com.example.unfold

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.rememberNavController
import com.example.unfold.data.Api
import com.example.unfold.data.CredentialsRepository
import com.example.unfold.data.models.room.AppDatabase
import com.example.unfold.ui.MainDestinations
import com.example.unfold.ui.NavGraph
import com.example.unfold.ui.common.AppBar
import com.example.unfold.ui.common.BottomBar
import com.example.unfold.ui.theme.Palette
import com.example.unfold.ui.theme.UnfoldTheme
import com.example.unfold.util.OnboardingCompleteKey
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var credentialsRepository: CredentialsRepository

    @Inject lateinit var prefs: SharedPreferences

    @Inject lateinit var db: AppDatabase

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

                val downloadCompletedReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                        if (id != null && id != -1L) {
                            coroutineScope.launch {
                                snackbarState.showSnackbar(getString(R.string.photo_downloaded))
                            }
                        }
                    }
                }

                val lifecycleOwner = LocalLifecycleOwner.current
                val context = LocalContext.current
                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_START -> {
                                context.registerReceiver(
                                    downloadCompletedReceiver,
                                    IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"),
                                )
                            }
                            Lifecycle.Event.ON_STOP -> {
                                context.unregisterReceiver(downloadCompletedReceiver)
                            }
                            else -> Unit
                        }
                    }

                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
                }

                var showLogoutSheet by remember { mutableStateOf(false) }

                Box {
                    Scaffold(
                        modifier = Modifier.run { if (showLogoutSheet) blur(10.dp) else this },
                        topBar = {
                            AppBar(
                                navController = navController,
                                title = title,
                            ) { showLogoutSheet = true }
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
                        containerColor = if (isSystemInDarkTheme()) Color(0xFF27292D) else Color.White,
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

                    BackHandler(showLogoutSheet) {
                        showLogoutSheet = false
                    }

                    if (showLogoutSheet) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                ) { showLogoutSheet = false },
                            color = Color.Black.copy(alpha = 0.5f),
                        ) { }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter),
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 22.dp, vertical = 34.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    text = stringResource(R.string.logout_message),
                                    textAlign = TextAlign.Center,
                                )
                                Row(modifier = Modifier.padding(top = 28.dp)) {
                                    Button(
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.LightGray,
                                            contentColor = Color.Black,
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        onClick = { showLogoutSheet = false },
                                    ) {
                                        Text(text = stringResource(R.string.no))
                                    }
                                    Button(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 9.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Palette.Green,
                                            contentColor = Color.White,
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        onClick = {
                                            coroutineScope.launch(Dispatchers.IO) {
                                                db.clearAllTables()
                                                credentialsRepository.token = null
                                                withContext(Dispatchers.Main) {
                                                    showLogoutSheet = false
                                                    navController.navigate(MainDestinations.LoginRoute) {
                                                        popUpTo(0)
                                                    }
                                                }
                                            }
                                        },
                                    ) {
                                        Text(text = stringResource(R.string.yes))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
