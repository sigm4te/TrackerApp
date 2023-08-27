package com.example.trackerapp.application

import android.app.Application
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class TrackerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME
    }
}