package com.interview.ideamanager.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tasks")
data class Task (

    @PrimaryKey(true)
    val id: Long? = null,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @ColumnInfo(name = "due_date")
    val dueDate: LocalDate? = null,
)