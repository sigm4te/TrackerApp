package com.example.trackerapp.utils

import android.location.Location
import com.example.trackerapp.database.entity.LocationEntity
import org.osmdroid.util.GeoPoint

fun Location.mapToEntity(): LocationEntity = LocationEntity(
    timestamp = time,
    lat = latitude,
    lon = longitude,
    accuracy = accuracy,
    altitude = altitude,
    bearing = bearing,
    provider = provider,
    speed = speed
)

fun LocationEntity.mapToGeoPoint(): GeoPoint = GeoPoint(lat, lon, altitude)