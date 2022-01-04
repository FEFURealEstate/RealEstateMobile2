package ru.fefu.nedviga.navigation.destinations

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.fefu.nedviga.ui.screens.splash.SplashScreen

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit,
) {
    composable(
        route = "splash",
    ) {
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}