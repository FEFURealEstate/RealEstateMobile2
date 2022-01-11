package ru.fefu.nedviga.data.repositories

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import ru.fefu.nedviga.data.ToDoDao
import ru.fefu.nedviga.data.models.ToDoTask
import ru.fefu.nedviga.data.network.ApiInterface
import ru.fefu.nedviga.data.network.App
import ru.fefu.nedviga.data.viewmodels.SharedViewModel
import javax.inject.Inject

@ViewModelScoped
class ToDoRepository @Inject constructor(private val toDoDao: ToDoDao) {
    val getAllTasks: Flow<List<ToDoTask>> = toDoDao.getAllTasks()
    val getMeetingTasks: Flow<List<ToDoTask>> = toDoDao.getMeetingTasks()
    val getPresentationTasks: Flow<List<ToDoTask>> = toDoDao.getPresentationTasks()
    val getPhoneCallTasks: Flow<List<ToDoTask>> = toDoDao.getPhoneCallTasks()

    fun getSelectedTask(taskId: Int) : Flow<ToDoTask> {
        return toDoDao.getSelectedTask(taskId = taskId)
    }

    suspend fun addTask(toDoTask: ToDoTask) {
        return toDoDao.addTask(toDoTask = toDoTask)
    }

    suspend fun updateTask(toDoTask: ToDoTask) {
        return toDoDao.updateTask(toDoTask = toDoTask)
    }

    suspend fun deleteTask(toDoTask: ToDoTask) {

        return toDoDao.deleteTask(toDoTask = toDoTask)
    }

    suspend fun deleteAllTasks() {
        return toDoDao.deleteAllTasks()
    }

    fun searchDatabase(searchQuery: String) : Flow<List<ToDoTask>> {
        return toDoDao.searchDatabase(searchQuery = searchQuery)
    }

}