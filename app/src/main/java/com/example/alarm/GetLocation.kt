package com.example.myapplication

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Context.LOCATION_SERVICE
class GetLocation: LocationListener {
    var lat = 0.0
    var lon = 0.0
    var locationManager: LocationManager? = null
    //locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    constructor(locationManager:LocationManager){
        this.locationManager = locationManager
    }
    override fun onLocationChanged(p0: Location?) {
        getLocation()
        lat = p0!!.latitude
        lon = p0!!.longitude
    }
    public fun getLonitude() : Double {
        return lon
    }
    public fun getLatitude() : Double {
        return lat
    }
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

    }

    override fun onProviderEnabled(p0: String?) {

    }

    override fun onProviderDisabled(p0: String?) {

    }


    fun getLocation() {
        try {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5.0f, this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }

    }
}