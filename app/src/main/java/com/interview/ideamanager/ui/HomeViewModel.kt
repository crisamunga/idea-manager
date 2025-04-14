package com.interview.ideamanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.ideamanager.R
import com.interview.ideamanager.data.Task
import com.interview.ideamanager.data.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val taskRepository: TaskRepository) : ViewModel() {

    private val chipSelection = MutableStateFlow(R.id.chip_all)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getAllTasks() = chipSelection.flatMapLatest { chipId ->
        when (chipId) {
            R.id.chip_all -> taskRepository.getAllTasks()
            R.id.chip_completed -> taskRepository.getTasksByState(true)
            R.id.chip_pending -> taskRepository.getTasksByState(false)
            else -> taskRepository.getAllTasks()
        }
    }

    fun toggleTask(task: Task) {
        task.id ?.let {
            viewModelScope.launch {
                taskRepository.updateState(it, !task.isCompleted)
            }
        }
    }

    fun filter(chipId: Int) {
        chipSelection.value = chipId
    }
}