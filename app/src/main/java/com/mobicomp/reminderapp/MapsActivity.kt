package com.mobicomp.reminderapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val TAG = MapsActivity::class.java.simpleName
    private val REQUEST_LOCATION_PERMISSION = 1
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0
    private var userId : Int = 0
    private var data: MutableList<User> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {

        val context = this
        val db = DBHandler(context)
        userId = intent.getIntExtra("userId", 0)
        data = db.readData()
        latitude = data[userId].latitude
        longitude = data[userId].longitude

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap
        val oulu = LatLng(latitude, longitude)
        val zoomLevel = 15f
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(oulu, zoomLevel))

        setMapLongClick(map)
        setPoiClick(map)
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
        enableMyLocation()
    }

    // Allow user to add virtual location
    @SuppressLint("ResourceType")
    private fun setMapLongClick(map: GoogleMap) {
        map.setOnMapLongClickListener {

            val context = this
            val db = DBHandler(context)
            val latitude = it.latitude
            val longitude = it.longitude

            db.insertVirtualLocation(userId, latitude, longitude)

            Toast.makeText(this@MapsActivity, "Virtual location selected:\nLatitude: " +
                    "$latitude\nLongitude: $longitude", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@MapsActivity, MenuActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener {
            val poiMarker = map.addMarker(MarkerOptions()
                    .position(it.latLng)
                    .title(it.name)
            )
            poiMarker.showInfoWindow()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode == REQUEST_LOCATION_PERMISSION) {
            if(grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }


}