package ru.fefu.nedviga.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.fefu.nedviga.data.models.TaskType
import ru.fefu.nedviga.data.models.ToDoTask

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY datetime ASC")
    fun getAllTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE id=:taskId")
    fun getSelectedTask(taskId: Int): Flow<ToDoTask>

    @Query("SELECT * FROM todo_table WHERE type LIKE 'm%' ORDER BY datetime ASC")
    fun getMeetingTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE type LIKE 'pr%' ORDER BY datetime ASC")
    fun getPresentationTasks(): Flow<List<ToDoTask>>

    @Query("SELECT * FROM todo_table WHERE type LIKE 'ph%' ORDER BY datetime ASC")
    fun getPhoneCallTasks(): Flow<List<ToDoTask>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(toDoTask: ToDoTask)

    @Update
    suspend fun updateTask(toDoTask: ToDoTask)

    @Delete
    suspend fun deleteTask(toDoTask: ToDoTask)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM todo_table WHERE comment LIKE :searchQuery ORDER BY datetime ASC")
    fun searchDatabase(searchQuery: String): Flow<List<ToDoTask>>

}