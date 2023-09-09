package com.example.trackerapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.trackerapp.database.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: LocationEntity): Long

    @Query("SELECT * FROM location ORDER BY timestamp ASC")
    suspend fun getLocations(): List<LocationEntity>

    @Query("SELECT * from location ORDER BY timestamp DESC LIMIT 1")
    fun getLastLocation(): LiveData<LocationEntity?>

    @Query("DELETE FROM location")
    suspend fun deleteLocations()
}