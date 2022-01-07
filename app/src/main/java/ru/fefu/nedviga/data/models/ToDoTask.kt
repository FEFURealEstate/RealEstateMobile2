package ru.fefu.nedviga.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.fefu.nedviga.util.Constants.DATABASE_TABLE

@Entity(tableName = DATABASE_TABLE)
data class ToDoTask (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val comment: String,
    val duration: Int,
    val type: TaskType,
    val uuid: String,
    val agent_id: Int,
    val datetime: Int,
    val date: String
)