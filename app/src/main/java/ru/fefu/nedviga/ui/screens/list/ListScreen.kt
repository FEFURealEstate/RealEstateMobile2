package ru.fefu.nedviga.ui.screens.list

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import ru.fefu.nedviga.ui.theme.Purple500
import ru.fefu.nedviga.ui.viewmodels.SharedViewModel
import ru.fefu.nedviga.util.SearchAppBarState

@ExperimentalMaterialApi
@Composable
fun ListScreen (
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
    }
    val action by sharedViewModel.action
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    sharedViewModel.handleDatabaseActions(action = action)
    Scaffold(
        topBar = {
             ListAppBar(
                 sharedViewModel = sharedViewModel,
                 searchAppBarState = searchAppBarState,
                 searchTextState = searchTextState
             )
        },
        content = {
            ListContent(
                tasks = allTasks,
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = Purple500
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Button",
            tint = Color.White
        )
    }
}