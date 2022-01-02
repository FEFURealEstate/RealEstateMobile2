package ru.fefu.nedviga.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.ui.theme.Typography

@Composable
fun TaskTypeItem(taskType: TaskType) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = taskType.color)
        }
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = taskType.name,
            style = Typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
@Preview
fun TakTypeItemPreview() {
    TaskTypeItem(taskType = TaskType.CALLING)
}