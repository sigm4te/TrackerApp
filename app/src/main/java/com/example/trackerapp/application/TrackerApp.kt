package com.example.trackerapp.application

import android.app.Application
import androidx.room.Room
import com.example.trackerapp.database.AppDatabase
import com.example.trackerapp.utils.ViewModelFactory
import org.osmdroid.config.Configuration
import org.osmdroid.library.BuildConfig

class TrackerApp : Application() {

    lateinit var database: AppDatabase
    lateinit var vmFactory: ViewModelFactory

    companion object {
        @Volatile private var INSTANCE: TrackerApp? = null
        fun instance(): TrackerApp = INSTANCE ?: synchronized(this) { TrackerApp() }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        Configuration.getInstance().userAgentValue = BuildConfig.LIBRARY_PACKAGE_NAME

        database = Room
            .databaseBuilder(instance(), AppDatabase::class.java, "database.db")
            .build()
        vmFactory = ViewModelFactory(database)
    }
}