package ru.fefu.nedviga.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fefu.nedviga.data.models.TaskType

@Composable
fun TaskTypeDropDown(
    taskType: TaskType,
    onTaskTypeSelected: (TaskType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(
                    alpha = ContentAlpha.disabled
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(16.dp)
                .weight(1f)
        ) {
            drawCircle(color = taskType.color)
        }
        Text(
            modifier = Modifier
                .weight(8f),
            text = taskType.name,
            style = MaterialTheme.typography.subtitle2
        )
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(weight = 1.5f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Drop-Down Arrow Icon"
            )
        }
        DropdownMenu(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onTaskTypeSelected(TaskType.MEETING)
            }) {
                TaskTypeItem(taskType = TaskType.MEETING)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onTaskTypeSelected(TaskType.SHOWING)
            }) {
                TaskTypeItem(taskType = TaskType.SHOWING)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onTaskTypeSelected(TaskType.CALLING)
            }) {
                TaskTypeItem(taskType = TaskType.CALLING)
            }
        }
    }
}


@Composable
@Preview
fun TaskTypeDropDownPreview() {
    TaskTypeDropDown(
        taskType = TaskType.MEETING,
        onTaskTypeSelected = {}
    )
}