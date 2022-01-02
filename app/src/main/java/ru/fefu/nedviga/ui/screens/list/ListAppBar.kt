package ru.fefu.nedviga.ui.screens.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import ru.fefu.nedviga.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.ui.theme.topAppBarBackgroundColor
import ru.fefu.nedviga.ui.theme.topAppBarContentColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.fefu.nedviga.components.TaskTypeItem
import ru.fefu.nedviga.ui.theme.Typography

@Composable
fun ListAppBar() {
    DefaultListAppBar(
        onSearchedClicked = {},
        onSortClicked = {},
        onDeleteClicked = {}
    )
}

@Composable
fun DefaultListAppBar(
    onSearchedClicked: () -> Unit,
    onSortClicked: (TaskType) -> Unit,
    onDeleteClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Tasks",
            color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            ListAppBarActions(
                onSearchedClicked = onSearchedClicked,
                onSortClicked = onSortClicked,
                onDeleteClicked = onDeleteClicked
            )
        },
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor
    )
}

@Composable
fun ListAppBarActions(
    onSearchedClicked: () -> Unit,
    onSortClicked: (TaskType) -> Unit,
    onDeleteClicked: () -> Unit
    ) {
    SearchAction(onSearchedClicked = onSearchedClicked)
    SortAction(onSortClicked = onSortClicked)
    DeleteAllAction(onDeleteClicked = onDeleteClicked)
}

@Composable
fun SearchAction(
    onSearchedClicked: () -> Unit
) {
    IconButton(onClick = { onSearchedClicked() }) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search Tasks",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (TaskType) -> Unit
) {
    var expanded by remember { mutableStateOf(false)}
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_filter_list),
            contentDescription = "Sort Tasks",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(TaskType.MEETING)
            }) {
                TaskTypeItem(taskType = TaskType.MEETING)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(TaskType.SHOWING)
            }) {
                TaskTypeItem(taskType = TaskType.SHOWING)
            }
            DropdownMenuItem(onClick = {
                expanded = false
                onSortClicked(TaskType.CALLING)
            }) {
                TaskTypeItem(taskType = TaskType.CALLING)
            }
        }
    }
}

@Composable
fun DeleteAllAction(
    onDeleteClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false)}
    IconButton(onClick = { expanded = true }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = "Delete All",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDeleteClicked()
            }) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Delete All",
                    style = Typography.subtitle2
                )
            }
        }
    }
}

@Composable
@Preview
private fun DefaultListAppBarPreview() {
    DefaultListAppBar(
        onSearchedClicked = {},
        onSortClicked = {},
        onDeleteClicked = {}
    )
}