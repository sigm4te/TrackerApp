package com.example.trackerapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackerapp.database.dao.LocationDao
import com.example.trackerapp.database.entity.LocationEntity

@Database(version = 1, exportSchema = false, entities = [LocationEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}