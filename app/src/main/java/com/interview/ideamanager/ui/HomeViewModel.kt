package com.interview.ideamanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.interview.ideamanager.data.TaskRepository

class HomeViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    fun getAllTasks() = taskRepository.getAllTasks()

    fun getTasksByState(isComplete: Boolean) = taskRepository.getTasksByState(isComplete)
}