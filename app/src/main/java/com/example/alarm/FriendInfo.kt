package com.example.alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.activity_friend_info.*


class FriendInfo : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        //        ads:adUnitId="ca-app-pub-2251557349292337/2994514292"

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }




}

