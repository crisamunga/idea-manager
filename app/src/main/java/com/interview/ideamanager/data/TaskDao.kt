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

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun delete(taskId: Long): Int

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun getTaskById(taskId: Long): Flow<Task>

    @Query("SELECT * FROM tasks WHERE is_completed = :isComplete")
    fun getTasksByState(isComplete: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>
}