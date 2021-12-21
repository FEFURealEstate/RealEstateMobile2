package ru.fefu.nedviga.navigation

import androidx.navigation.NavHostController
import ru.fefu.nedviga.util.Action
import ru.fefu.nedviga.util.Constants.LIST_SCREEN

class Screens(navController: NavHostController) {
    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }
    val task: (Int) -> Unit = { taskId ->
        navController.navigate("task/$taskId")
    }
}