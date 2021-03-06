package ru.fefu.nedviga.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.fefu.nedviga.navigation.destinations.listComposable
import ru.fefu.nedviga.navigation.destinations.splashComposable
import ru.fefu.nedviga.navigation.destinations.taskComposable
import ru.fefu.nedviga.data.viewmodels.SharedViewModel
import ru.fefu.nedviga.util.Constants.SPLASH_SCREEN

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavigation(navController: NavHostController, sharedViewModel: SharedViewModel) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(navigateToListScreen = screen.splash)
        listComposable(navigateToTaskScreen = screen.list, sharedViewModel = sharedViewModel)
        taskComposable(navigateToListScreen = screen.task, sharedViewModel = sharedViewModel)
    }
}