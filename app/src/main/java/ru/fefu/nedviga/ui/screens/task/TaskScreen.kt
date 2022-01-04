package ru.fefu.nedviga.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.ui.viewmodels.SharedViewModel
import ru.fefu.nedviga.util.Action

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
){
    val comment: String by sharedViewModel.comment
    val description: String by sharedViewModel.description
    val taskType: TaskType by sharedViewModel.taskType
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION) {
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()) {
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }
                }
            )
         },
        content = {
            TaskContent(
                comment = comment,
                onCommentChange = { sharedViewModel.comment.value = it },
                description = description,
                onDescriptionChange = { sharedViewModel.description.value = it },
                taskType = taskType,
                onTaskTypeSelected = { sharedViewModel.taskType.value = it }
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields are empty",
        Toast.LENGTH_SHORT
    ).show()
}