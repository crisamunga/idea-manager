package com.interview.ideamanager.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.ideamanager.R
import com.interview.ideamanager.data.Task
import com.interview.ideamanager.data.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class FormState(
    @StringRes
    val titleError: Int? = null,
    @StringRes
    val descriptionError: Int? = null,
    @StringRes
    val dueDateError: Int? = null,
    val isValid: Boolean = false
)

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _formState = MutableStateFlow(FormState())
    val formState: StateFlow<FormState> = _formState

    var taskId: Long? = null
    var title: String? = null
    var description: String? = null
    var isCompleted: Boolean = false
    var dueDate: LocalDate? = null

    fun validate() : Boolean {
        var titleError: Int? = null
        var descriptionError: Int? = null
        var dueDateError: Int? = null
        var valid = true

        if (title.isNullOrEmpty()) {
            titleError = R.string.this_field_is_required
            valid = false
        } else {
            titleError = null
        }

        if (description.isNullOrEmpty()) {
            descriptionError = R.string.this_field_is_required
            valid = false
        } else {
            descriptionError = null
        }

        if (dueDate == null) {
            dueDateError = R.string.this_field_is_required
            valid = false
        } else {
            dueDateError = null
        }

        _formState.value = _formState.value.copy(
            titleError = titleError,
            descriptionError = descriptionError,
            dueDateError = dueDateError,
            isValid = valid
        )

        return valid
    }

    fun save() {
        if (taskId == null) {
            insert()
        } else {
            update()
        }
    }

    fun toggle() {
        isCompleted = !isCompleted
        save()
    }

    private fun insert() {
        if (!validate()) return
        val task = Task(
            id = taskId,
            title = title ?: "",
            description = description,
            isCompleted = isCompleted,
            dueDate = dueDate
        )
        viewModelScope.launch {
            taskRepository.insert(task)
        }
    }

    private fun update() {
        if (!validate()) return
        taskId ?.let {
            val task = Task(
                id = taskId,
                title = title ?: "",
                description = description,
                isCompleted = isCompleted,
                dueDate = dueDate
            )
            viewModelScope.launch {
                taskRepository.update(task)
            }
        }
    }

    fun delete() {
        taskId ?.let { taskId ->
            viewModelScope.launch {
                taskRepository.delete(taskId)
            }
        }
    }

    fun getTaskById(taskId: Long) = taskRepository.getTaskById(taskId)

    fun getAllTasks() = taskRepository.getAllTasks()
}