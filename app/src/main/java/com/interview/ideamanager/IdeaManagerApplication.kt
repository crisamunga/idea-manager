package com.interview.ideamanager

import android.app.Application
import com.interview.ideamanager.data.AppContainer
import com.interview.ideamanager.data.AppDataContainer

class IdeaManagerApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppDataContainer(this)
    }
}