package com.example.trackerapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trackerapp.utils.SingleLiveEvent

class MainViewModel : ViewModel() {

    private val _permissionsGranted: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _locationEnabled: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _mapReady: MutableLiveData<Boolean> = SingleLiveEvent()
    private val _trackingReady: MutableLiveData<Boolean> = MutableLiveData()

    val permissionsGranted: LiveData<Boolean> = _permissionsGranted
    val locationEnabled: LiveData<Boolean> = _locationEnabled
    val mapReady: LiveData<Boolean> = _mapReady
    val trackingReady: LiveData<Boolean> = _trackingReady

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
}