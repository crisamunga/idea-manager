package com.interview.ideamanager.data

import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: Task): Long {
        return taskDao.insert(task)
    }

    suspend fun update(task: Task): Int {
        return taskDao.update(task)
    }

    suspend fun delete(task: Task): Int {
        return taskDao.delete(task)
    }

    suspend fun getTaskById(taskId: Long): Flow<Task> {
        return taskDao.getTaskById(taskId)
    }


    suspend fun getTasksByState(isComplete: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByState(isComplete)
    }

    suspend fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

}