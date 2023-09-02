package com.example.trackerapp.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import com.example.trackerapp.R

class LocationForegroundService : LifecycleService() {

    private val manager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    companion object {
        private const val CHANNEL_ID = "location_channel"
        private const val CHANNEL_NAME = "Location Channel"
        private const val NOTIFICATION_ID = 121
    }

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
        manager.createNotificationChannel(channel)
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
            manager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun startLocation() {

    }
}