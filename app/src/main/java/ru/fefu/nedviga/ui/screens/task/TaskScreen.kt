package ru.fefu.nedviga.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import ru.fefu.nedviga.util.Action

@Composable
fun TaskScreen(
    navigateToListScreen: (Action) -> Unit
){
    Scaffold(
        topBar = {
            TaskAppBar(navigateToListScreen = navigateToListScreen)
         },
        content = {}
    )
}