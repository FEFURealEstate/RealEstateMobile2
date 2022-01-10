package ru.fefu.nedviga.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.fefu.nedviga.components.TaskTypeDropDown
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.ui.screens.date.SpinnerViewModel
import ru.fefu.nedviga.ui.theme.Purple500
import ru.fefu.nedviga.ui.theme.fabBackground
import ru.fefu.nedviga.ui.theme.inColor
import ru.fefu.nedviga.ui.theme.taskItemTextColor

@Composable
fun TaskContent(
    comment: String,
    onCommentChange: (String) -> Unit,
    duration: Int,
    onDurationChange: (Int) -> Unit,
    taskType: TaskType,
    onTaskTypeSelected: (TaskType) -> Unit,
    datetime: Int,
    onDatetimeChange: (Int) -> Unit,
    date: String,
    onDateChange: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: SpinnerViewModel = viewModel()
    val dateTime = viewModel.time.observeAsState()
    val convertedDate = viewModel.converted.observeAsState()

    var durationText = ""
    if (duration != 0) durationText = duration.toString()

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
        TaskTypeDropDown(
            taskType = taskType,
            onTaskTypeSelected = onTaskTypeSelected
        )
        Divider(
            modifier = Modifier.height(8.dp),
            color = MaterialTheme.colors.background
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row{
            TextButton(
                onClick = {
                    viewModel.selectDateTime(context)
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colors.fabBackground)
                    .padding(5.dp)
            ) {
                Text(text = "Select Date", color = MaterialTheme.colors.inColor)
            }
            if (dateTime.value == "")
                Text(text = date, Modifier.padding(20.dp, 12.dp), color = MaterialTheme.colors.taskItemTextColor)
            else {
                Text(text = dateTime.value!!, Modifier.padding(20.dp, 12.dp), color = MaterialTheme.colors.taskItemTextColor)
                onDatetimeChange(convertedDate.value!!.toInt())
                onDateChange(dateTime.value!!)
            }
        }
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
        datetime = 1641038400,
        onDatetimeChange = {},
        date = "",
        onDateChange = {}
    )
}
