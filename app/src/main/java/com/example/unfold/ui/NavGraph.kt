package com.example.unfold.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.example.unfold.R
import com.example.unfold.ui.common.CollectionsRoutes
import com.example.unfold.ui.common.HomeTabs
import com.example.unfold.ui.common.RibbonRoutes
import com.example.unfold.ui.screens.auth.AuthScreen
import com.example.unfold.ui.screens.collection.CollectionScreen
import com.example.unfold.ui.screens.collections.CollectionsScreen
import com.example.unfold.ui.screens.login.LoginScreen
import com.example.unfold.ui.screens.onboarding.OnboardingScreen
import com.example.unfold.ui.screens.photo.PhotoScreen
import com.example.unfold.ui.screens.profile.ProfileScreen
import com.example.unfold.ui.screens.ribbon.RibbonScreen
import com.example.unfold.util.RedirectUri

object MainDestinations {
    const val OnboardingRoute = "onboarding"
    const val LoginRoute = "login"
    const val AuthRoute = "auth/{code}"
    const val HomeRoute = "home"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    paddingValues: PaddingValues,
    login: () -> Unit,
    setTitle: (String) -> Unit,
    showSnackbar: (String) -> Unit,
) {
    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(route = MainDestinations.OnboardingRoute) {
            OnboardingScreen {
                navController.navigate(MainDestinations.LoginRoute) { popUpTo(0) }
            }
        }
        composable(route = MainDestinations.LoginRoute) { LoginScreen(login) }
        composable(
            route = MainDestinations.AuthRoute,
            deepLinks = listOf(navDeepLink { uriPattern = "$RedirectUri?code={code}" }),
        ) {
            AuthScreen(showSnackbar) {
                navController.navigate(MainDestinations.HomeRoute) { popUpTo(0) }
            }
        }
        navigation(
            route = MainDestinations.HomeRoute,
            startDestination = HomeTabs.Ribbon.route,
        ) {
            composable(route = HomeTabs.Ribbon.route) {
                val title = stringResource(R.string.ribbon)
                LaunchedEffect(Unit) { setTitle(title) }
                RibbonScreen(showSnackbar) {
                    navController.navigate(RibbonRoutes.PhotoRoute + "/$it")
                }
            }
            composable(
                route = RibbonRoutes.PhotoRoute + "/{${RibbonRoutes.PhotoId}}",
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://unsplash.com/photos/{${RibbonRoutes.PhotoId}}"
                    },
                ),
            ) {
                val title = stringResource(R.string.photo)
                LaunchedEffect(Unit) { setTitle(title) }
                PhotoScreen(showSnackbar)
            }
            composable(route = HomeTabs.Collections.route) {
                val title = stringResource(R.string.collections)
                LaunchedEffect(Unit) { setTitle(title) }
                CollectionsScreen {
                    navController.navigate(CollectionsRoutes.CollectionRoute + "/$it")
                }
            }
            composable(
                route = CollectionsRoutes.CollectionRoute + "/{${CollectionsRoutes.CollectionId}}",
            ) {
                CollectionScreen(setTitle, showSnackbar) {
                    navController.navigate(RibbonRoutes.PhotoRoute + "/$it")
                }
            }
            composable(route = HomeTabs.Profile.route) {
                val title = stringResource(R.string.profile)
                LaunchedEffect(Unit) { setTitle(title) }
                ProfileScreen(showSnackbar) {
                    navController.navigate(RibbonRoutes.PhotoRoute + "/$it")
                }
            }
        }
    }
}
