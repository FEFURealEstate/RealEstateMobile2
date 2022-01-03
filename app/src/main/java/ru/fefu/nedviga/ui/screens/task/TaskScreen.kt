package ru.fefu.nedviga.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.util.Action

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    navigateToListScreen: (Action) -> Unit
){
    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
         },
        content = {}
    )
}