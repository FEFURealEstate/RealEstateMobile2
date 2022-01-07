package ru.fefu.nedviga.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fefu.nedviga.components.TaskTypeDropDown
import ru.fefu.nedviga.data.models.TaskType

@Composable
fun TaskContent(
    comment: String,
    onCommentChange: (String) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    taskType: TaskType,
    onTaskTypeSelected: (TaskType) -> Unit,
    datetime: Int,
    onDatetimeChange: (Int) -> Unit
) {
    var durationText = ""
    if (duration != 0) durationText = duration.toString()
    var datetimeText = ""
    if (datetime != 0) datetimeText = datetime.toString()

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
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = durationText,
            onValueChange = {
                if (it == "") onDurationChange(0)
                else onDurationChange(it.toInt())
            },
            label = { Text(text = "Duration") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Number)
        )
        Divider(
            modifier = Modifier.height(8.dp),
            color = MaterialTheme.colors.background
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = datetimeText,
            onValueChange = {
                if (it == "") onDatetimeChange(0)
                else onDatetimeChange(it.toInt())
            },
            label = { Text(text = "Datetime") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true,
            keyboardOptions = KeyboardOptions (keyboardType = KeyboardType.Number)
        )
        Divider(
            modifier = Modifier.height(8.dp),
            color = MaterialTheme.colors.background
        )
        TaskTypeDropDown(
            taskType = taskType,
            onTaskTypeSelected = onTaskTypeSelected
        )
    }
}


@Composable
@Preview
private fun TaskContentPreview() {
    TaskContent(
        comment = "",
        onCommentChange = {},
        duration = 0,
        onDurationChange = {},
        taskType = TaskType.meeting,
        onTaskTypeSelected = {},
        datetime = 0,
        onDatetimeChange = {},
    )
}