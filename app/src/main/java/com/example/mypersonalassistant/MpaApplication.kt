package com.example.mypersonalassistant

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MpaApplication : Application() {

    companion object {
        lateinit var resources: Resources
    }

    override fun onCreate() {
        super.onCreate()
        MpaApplication.resources = this.resources
    }
}