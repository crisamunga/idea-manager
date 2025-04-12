package com.interview.ideamanager.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.interview.ideamanager.IdeaManagerApplication


object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for AddEditTaskViewModel
        initializer {
            AddEditTaskViewModel(
                this.ideaManagerApplication().appContainer.taskRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(
                this.ideaManagerApplication().appContainer.taskRepository
            )
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [IdeaManagerApplication].
 */
fun CreationExtras.ideaManagerApplication(): IdeaManagerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as IdeaManagerApplication)
