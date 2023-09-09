package com.example.trackerapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.trackerapp.database.AppDatabase
import com.example.trackerapp.ui.main.MainViewModel

class ViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(db.locationDao())
            else -> throw IllegalArgumentException("Cannot find $modelClass")
        } as T
    }
}