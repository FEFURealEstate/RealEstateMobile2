package ru.fefu.nedviga.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fefu.nedviga.components.TaskTypeDropDown
import ru.fefu.nedviga.data.models.TaskType

@Composable
fun TaskContent(
    comment: String,
    onCommentChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    taskType: TaskType,
    onTaskTypeSelected: (TaskType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = 12.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = comment,
            onValueChange = { onCommentChange(it) },
            label = { Text(text = "Comment") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Divider(
            modifier = Modifier.height(8.dp),
            color = MaterialTheme.colors.background
        )
        TaskTypeDropDown(
            taskType = taskType,
            onTaskTypeSelected = onTaskTypeSelected
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = "Description") },
            textStyle = MaterialTheme.typography.body1
        )
    }
}


@Composable
@Preview
private fun TaskContentPreview() {
    TaskContent(
        comment = "",
        onCommentChange = {},
        description = "",
        onDescriptionChange = {},
        taskType = TaskType.MEETING,
        onTaskTypeSelected = {}
    )
}