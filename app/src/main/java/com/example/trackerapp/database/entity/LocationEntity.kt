package com.example.trackerapp.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val lat: Double,
    val lon: Double,
    val accuracy: Float,
    val altitude: Double,
    val bearing: Float,
    val provider: String?,
    val speed: Float
)