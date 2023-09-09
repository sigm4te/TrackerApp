package com.example.trackerapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.trackerapp.R
import com.example.trackerapp.application.TrackerApp
import com.example.trackerapp.database.AppDatabase
import com.example.trackerapp.database.entity.LocationEntity
import kotlinx.coroutines.launch

class LocationForegroundService : LifecycleService() {

    companion object {
        private const val CHANNEL_ID = "location_channel"
        private const val CHANNEL_NAME = "Location Channel"
        private const val NOTIFICATION_ID = 121
    }

    private val notificationManager: NotificationManager by lazy {
        TrackerApp.instance().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private val locationManager: LocationManager by lazy {
        TrackerApp.instance().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    private val db: AppDatabase by lazy {
        TrackerApp.instance().database
    }

    private val distance: Long = 5_000L
    private val time: Float = 5.0f

    override fun onCreate() {
        super.onCreate()

        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            startService()
            startLocation()
        } else {
            stopForeground(true)
        }
    }

    private fun startService() {
        createChannel()
        createNotification()
    }

    private fun createChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setShowBadge(false)
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification() {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setOngoing(true)
            .setSilent(true)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_message))
            .setSmallIcon(R.drawable.ic_stat_location)
            .setColor(ContextCompat.getColor(this, R.color.red))
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(NOTIFICATION_ID, notification)
        } else {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, distance, time) { location ->
            onNewLocation(location)
        }
    }

    private fun onNewLocation(location: Location) {
        lifecycleScope.launch {
            db.locationDao().insert(location.mapToEntity())
        }
    }
}

private fun Location.mapToEntity() = LocationEntity(
    timestamp = time,
    lat = latitude,
    lon = longitude,
    accuracy = accuracy,
    altitude = altitude,
    bearing = bearing,
    provider = provider,
    speed = speed
)