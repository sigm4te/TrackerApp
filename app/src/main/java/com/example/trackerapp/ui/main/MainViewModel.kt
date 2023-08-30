package com.example.trackerapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trackerapp.utils.SingleLiveEvent
import org.osmdroid.util.GeoPoint

class MainViewModel : ViewModel() {

    private val pointList = mutableListOf<GeoPoint>()

    private val _permissionsGranted: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _locationEnabled: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _mapReady: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _trackingReady: MutableLiveData<Boolean> = MutableLiveData()
    private val _points: MutableLiveData<List<GeoPoint>> = MutableLiveData()

    val permissionsGranted: LiveData<Boolean> = _permissionsGranted
    val locationEnabled: LiveData<Boolean> = _locationEnabled
    val mapReady: LiveData<Boolean> = _mapReady
    val trackingReady: LiveData<Boolean> = _trackingReady
    val points: LiveData<List<GeoPoint>> = _points

    fun onRequestResult(status: Boolean) {
        if (status) onPermissionsGranted()
    }

    fun onPermissionsGranted() {
        _permissionsGranted.value = true
    }

    fun onLocationEnabled() {
        _locationEnabled.value = true
    }

    fun onMapReady() {
        _mapReady.value = true
    }

    fun onTrackingReady() {
        _trackingReady.value = true
    }

    fun onTrackingResult(point: GeoPoint) {
        pointList.add(point)
        _points.value = pointList
    }
}