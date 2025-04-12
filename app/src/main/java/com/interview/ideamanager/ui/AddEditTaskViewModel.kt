package com.interview.ideamanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.interview.ideamanager.data.TaskRepository

class AddEditTaskViewModel(repository: TaskRepository) : ViewModel()