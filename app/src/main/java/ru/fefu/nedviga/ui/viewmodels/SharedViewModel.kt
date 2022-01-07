package ru.fefu.nedviga.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.data.repositories.DataStoreRepository
import ru.fefu.nedviga.data.repositories.ToDoRepository
import ru.fefu.nedviga.util.Action
import ru.fefu.nedviga.util.RequestState
import ru.fefu.nedviga.util.SearchAppBarState
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableState<Int> = mutableStateOf(0)
    val comment: MutableState<String> = mutableStateOf("")
    val duration: MutableState<Int> = mutableStateOf(0)
    val taskType: MutableState<TaskType> = mutableStateOf(TaskType.meeting)
    val uuid: MutableState<String> = mutableStateOf("")
    val agentId: MutableState<Int> = mutableStateOf(0)
    val datetime: MutableState<Int> = mutableStateOf(0)
    val date: MutableState<String> = mutableStateOf("")

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    fun searchDatabase(searchQuery: String) {
        _searchedTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.searchDatabase(searchQuery = "%$searchQuery%")
                    .collect { searchedTasks ->
                        _searchedTasks.value = RequestState.Success(searchedTasks)
                    }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGERED
    }

    val meetingTasks: StateFlow<List<ToDoTask>> =
        repository.getMeetingTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val presentationTasks: StateFlow<List<ToDoTask>> =
        repository.getPresentationTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val phoneCallTasks: StateFlow<List<ToDoTask>> =
        repository.getPhoneCallTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _sortState =
        MutableStateFlow<RequestState<TaskType>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<TaskType>> = _sortState

    fun readSortState() {
        _sortState.value = RequestState.Loading
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map { TaskType.valueOf(it) }
                    .collect {
                        _sortState.value = RequestState.Success(it)
                    }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun persistSortState(taskType: TaskType) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.persistSortState(taskType = taskType)
        }
    }

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                repository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                comment = comment.value,
                duration = duration.value,
                type = taskType.value,
                uuid = uuid.value,
                agent_id = agentId.value,
                datetime = datetime.value,
                date = date.value
            )
            repository.addTask(toDoTask = toDoTask)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                comment = comment.value,
                duration = duration.value,
                type = taskType.value,
                uuid = uuid.value,
                agent_id = agentId.value,
                datetime = datetime.value,
                date = date.value
            )
            repository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                comment = comment.value,
                duration = duration.value,
                type = taskType.value,
                uuid = uuid.value,
                agent_id = agentId.value,
                datetime = datetime.value,
                date = date.value
            )
            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasks()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {}
        }
        this.action.value = Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            comment.value = selectedTask.comment
            duration.value = selectedTask.duration
            taskType.value = selectedTask.type
            uuid.value = selectedTask.uuid
            agentId.value = selectedTask.agent_id
            datetime.value = selectedTask.datetime
            date.value = selectedTask.date
        } else {
            id.value = 0
            comment.value = ""
            duration.value = 0
            taskType.value = TaskType.meeting
            uuid.value = ""
            agentId.value = 3
            datetime.value = 123
            date.value = ""
        }
    }

    fun validateFields(): Boolean {
        return comment.value.isNotEmpty() and (datetime.value != 0)
    }
}