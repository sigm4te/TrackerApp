package com.example.trackerapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackerapp.database.dao.LocationDao
import com.example.trackerapp.utils.SingleLiveEvent
import com.example.trackerapp.utils.mapToGeoPoint
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class MainViewModel(private val locationDao: LocationDao) : ViewModel() {

    private val pointList = mutableListOf<GeoPoint>()

    private val _permissionsGranted: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _locationEnabled: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _mapReady: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _trackingReady: MutableLiveData<Boolean> = MutableLiveData()
    private val _points: MutableLiveData<List<GeoPoint>> = MutableLiveData()
    private val _lastLocation: MediatorLiveData<GeoPoint> = MediatorLiveData()

    val permissionsGranted: LiveData<Boolean> = _permissionsGranted
    val locationEnabled: LiveData<Boolean> = _locationEnabled
    val mapReady: LiveData<Boolean> = _mapReady
    val trackingReady: LiveData<Boolean> = _trackingReady
    val points: LiveData<List<GeoPoint>> = _points
    val lastLocation: LiveData<GeoPoint> = _lastLocation

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

        viewModelScope.launch {
            pointList.clear()
            val list = locationDao.getLocations().map { it.mapToGeoPoint() }
            pointList.addAll(list)
            _points.value = list
        }
    }

    fun onTrackingReady() {
        _trackingReady.value = true
    }

    fun onTrackingResult() {
        viewModelScope.launch {
            _lastLocation.addSource(locationDao.getLastLocation()) {
                it?.let { entity ->
                    val point = entity.mapToGeoPoint()
                    _lastLocation.value = point
                    pointList.add(point)
                }
                _points.value = pointList
            }
        }
    }

    fun onTrackingStop() {
        _trackingReady.value = false
    }
}