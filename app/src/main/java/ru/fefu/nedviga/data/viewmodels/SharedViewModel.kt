package ru.fefu.nedviga.data.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.fefu.nedviga.MainActivity
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.data.network.ApiInterface
import ru.fefu.nedviga.data.network.App
import ru.fefu.nedviga.data.repositories.DataStoreRepository
import ru.fefu.nedviga.data.repositories.ToDoRepository
import ru.fefu.nedviga.util.*
import java.io.Console
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val dataStoreRepository: DataStoreRepository,
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
    val _flag = MutableLiveData(false)
    var flag: LiveData<Boolean> = _flag

    val searchAppBarState: MutableState<SearchAppBarState> =
        mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    private val _searchedTasks =
        MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val activityApi = App.INSTANCE.retrofit.create(ApiInterface::class.java)

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

    fun setAllTasks(agentId: Int) {
        try {
            viewModelScope.launch {
                deleteAllTasks()
                App.INSTANCE.sharedPreferences.edit().putInt("agentId", agentId).apply()
                val data = activityApi.getEvents(agentId = agentId)
                data.forEach { item ->
                    repository.addTask(item)
                }
            }
        } catch (e: Exception) {

        }
    }

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

    private fun scheduleNotification()
    {
        val intent = Intent(App.INSTANCE.applicationContext, Notification::class.java)
        intent.putExtra(titleExtra, "You scheduled ${comment.value}")
        intent.putExtra(messageExtra, "Duration: ${duration.value} min.")

        val pendingIntent = PendingIntent.getBroadcast(
            App.INSTANCE.applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = App.INSTANCE.applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager
        val time = datetime.value.toLong() * 1000 - 600000
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun addTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = activityApi.createEvent(
                agentId = agentId.value,
                datetime = datetime.value,
                date = date.value,
                duration = duration.value,
                type = taskType.value.name,
                comment = comment.value
            )
            scheduleNotification()
            repository.addTask(toDoTask = data)
        }
        searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTaskAPI() {
        try {
            viewModelScope.launch {
                activityApi.updateEvent(
                    uuid = uuid.value,
                    agentId = agentId.value,
                    datetime = datetime.value,
                    date = date.value,
                    duration = duration.value,
                    type = taskType.value.name,
                    comment = comment.value
                )
            }
        } catch (e: Exception) {

        }
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                comment = comment.value,
                duration = duration.value,
                type = taskType.value,
                agentId = agentId.value,
                datetime = datetime.value,
                date = date.value,
                uuid = uuid.value
            )
            repository.updateTask(toDoTask = toDoTask)
        }
    }

    private fun deleteTaskAPI(uuid: String) {
        try {
            viewModelScope.launch {
                activityApi.deleteEvent(uuid)
            }
        } catch (e: Exception) {

        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id.value,
                comment = comment.value,
                duration = duration.value,
                type = taskType.value,
                agentId = agentId.value,
                datetime = datetime.value,
                date = date.value,
                uuid = uuid.value
            )
            repository.deleteTask(toDoTask = toDoTask)
        }
    }

    private fun deleteAllTasksAPI() {
        try {
            viewModelScope.launch {
                activityApi.deleteAllEvents(agentId = agentId.value)
            }
        } catch(e: Exception) {

        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    private fun exit() {
        viewModelScope.launch(Dispatchers.IO) {
            _flag.postValue(true)
        }
    }

    fun handleDatabaseActions(action: Action) {
        when (action) {
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTaskAPI()
                updateTask()
            }
            Action.DELETE -> {
                deleteTaskAPI(uuid.value)
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTasksAPI()
                deleteAllTasks()
            }
            Action.EXIT -> {
                exit()
            }
            Action.UNDO -> {
                addTask()
            }
            else -> {
            }
        }
        this.action.value = Action.NO_ACTION
    }

    fun updateTaskFields(selectedTask: ToDoTask?) {
        if (selectedTask != null) {
            id.value = selectedTask.id
            comment.value = selectedTask.comment
            duration.value = selectedTask.duration
            taskType.value = selectedTask.type
            agentId.value = selectedTask.agentId
            datetime.value = selectedTask.datetime
            date.value = selectedTask.date
            uuid.value = selectedTask.uuid
        } else {
            id.value = 0
            comment.value = ""
            duration.value = 0
            taskType.value = TaskType.meeting
            uuid.value = ""
            agentId.value = App.INSTANCE.sharedPreferences.getInt("agentId", 300)
            datetime.value = 123
            date.value = ""
        }
    }

    fun validateFields(): Boolean {
        return comment.value.isNotEmpty() and (datetime.value != 0) and (date.value != "")
    }
}