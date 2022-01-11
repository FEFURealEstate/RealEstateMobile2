package ru.fefu.nedviga.ui.screens.list

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Money
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.launch
import ru.fefu.nedviga.R
import ru.fefu.nedviga.ui.theme.fabBackground
import ru.fefu.nedviga.data.viewmodels.SharedViewModel
import ru.fefu.nedviga.ui.theme.inColor
import ru.fefu.nedviga.util.Action
import ru.fefu.nedviga.util.SearchAppBarState

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun ListScreen(
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }
    val action by sharedViewModel.action
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val meetingTasks by sharedViewModel.meetingTasks.collectAsState()
    val presentationTasks by sharedViewModel.presentationTasks.collectAsState()
    val phoneCallTasks by sharedViewModel.phoneCallTasks.collectAsState()
    val searchAppBarState: SearchAppBarState by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState
    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = { sharedViewModel.handleDatabaseActions(action = action) },
        onUndoClicked = { sharedViewModel.action.value = it },
        taskComment = sharedViewModel.comment.value,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                sortState = sortState,
                meetingTasks = meetingTasks,
                presentationTasks = presentationTasks,
                phoneCallTasks = phoneCallTasks,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = { action, task ->
                    sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(selectedTask = task)
                },
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
        backgroundColor = MaterialTheme.colors.fabBackground
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Button",
            tint = MaterialTheme.colors.inColor
        )
    }
}

@Composable
fun TestFab() {
    val context = LocalContext.current
    FloatingActionButton(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "channel"
                val descriptionText = "channel_d"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel("channe1", name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            var builder = NotificationCompat.Builder(context, "channe1")
                .setSmallIcon(R.drawable.ic_logo_dark)
                .setContentTitle("My notification")
                .setContentText("Событие началось")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            var notificationManager = NotificationManagerCompat.from(context)

            notificationManager.notify(1, builder.build())
        },
        backgroundColor = MaterialTheme.colors.fabBackground
    ) {
        Icon(
            imageVector = Icons.Filled.Money,
            contentDescription = "Add Button",
            tint = MaterialTheme.colors.inColor
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: () -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskComment: String,
    action: Action
) {
    handleDatabaseActions()

    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action != Action.NO_ACTION) {
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskComment = taskComment),
                    actionLabel = setActionLabel(action = action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setMessage(action: Action, taskComment: String): String {
    return when (action) {
        Action.DELETE_ALL -> "All tasks were removed"
        else -> "${action.name}: $taskComment"
    }
}

private fun createNotificationChannel() {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library

}

private fun setActionLabel(action: Action): String {
    return if (action.name == "DELETE") {
        "UNDO"
    } else {
        "OK"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
) {
    if (snackBarResult == SnackbarResult.ActionPerformed
        && action == Action.DELETE
    ) {
        onUndoClicked(Action.UNDO)
    }
}