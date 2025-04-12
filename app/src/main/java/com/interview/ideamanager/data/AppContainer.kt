package com.interview.ideamanager.data

import android.content.Context

interface AppContainer {
    val taskRepository: TaskRepository
}

/**
 * [AppContainer] implementation that provides instance of [TaskRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [TaskRepository]
     */
    override val taskRepository: TaskRepository by lazy {
        val database = TaskDatabase.getDatabase(context)
        TaskRepository(database.taskDao())
    }
}
