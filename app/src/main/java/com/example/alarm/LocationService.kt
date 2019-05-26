package com.example.alarm

import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import bolts.Task.delay
import com.example.myapplication.GetLocation
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

class LocationService : IntentService("LocationService") {
    override fun onHandleIntent(intent: Intent?) {
        var locationGetter = GetLocation(getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        locationGetter.getLocation()

    }
}