package com.interview.ideamanager.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = IGNORE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task): Int

    @Delete
    suspend fun delete(task: Task): Int

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: Long): Flow<Task>

    @Query("SELECT * FROM tasks WHERE is_completed = :isComplete")
    suspend fun getTasksByState(isComplete: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): Flow<List<Task>>
}