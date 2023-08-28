package com.example.trackerapp.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trackerapp.R
import com.example.trackerapp.databinding.FragmentMainBinding
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var map: MapView
    private lateinit var locationManager: LocationManager

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        requestLocationPermission()
        checkLocationStatus()

        map = binding.map
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setBuiltInZoomControls(true)
        map.setMultiTouchControls(true)

        val mapController: IMapController = map.controller
        val startPoint = GeoPoint(48.8583, 2.2944)
        mapController.setZoom(9.5)
        mapController.setCenter(startPoint)
    }

    private fun requestLocationPermission() {
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {}
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {}
                else -> {}
            }
        }.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))
    }

    private fun checkLocationStatus() {
        if (!isLocationEnabled()) {
            AlertDialog.Builder(requireContext())
                .setTitle(R.string.gps_not_found_title)
                .setMessage(R.string.gps_not_found_message)
                .setPositiveButton(R.string.gps_yes) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton(R.string.gps_no, null)
                .show();
        }
    }

    private fun isLocationEnabled(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // API >= 28 / Android 9+
            locationManager.isLocationEnabled
        } else {
            // API < 28
            val mode: Int = Settings.Secure.getInt(requireContext().contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
            (mode != Settings.Secure.LOCATION_MODE_OFF)
        }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }
}