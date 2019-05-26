package com.example.alarm

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.maps.*
import com.parse.FunctionCallback
import com.parse.ParseCloud
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_friend_info.*
import kotlinx.android.synthetic.main.activity_friends_list.*
import org.json.JSONArray
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FriendInfo : AppCompatActivity(),OnMapReadyCallback {
    var isFriend  : Boolean = false

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        //        ads:adUnitId="ca-app-pub-2251557349292337/2994514292"
        System.out.println(intent.getStringExtra("username"))
        //check if searched user is our friend
        System.out.println("HEJ"+findFollowers(intent.getStringExtra("username")).toString())
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findFollowers(intent.getStringExtra("username"))






    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setLocation(50.829,19.1377)
    }

    fun findFollowers(name : String) {
        val parametersForFollowers = HashMap<String, String>()
        parametersForFollowers.put("user", ParseUser.getCurrentUser().objectId.toString())
        ParseCloud.callFunctionInBackground("getFollowers", parametersForFollowers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    for (i in 0 until followers.size) {
                        var jsonArray = JSONArray(followers.toString())
                        for (j in 0 until jsonArray.length()) {
                            if(jsonArray.getJSONObject(j).get("username").toString().equals(name))
                            {
                                System.out.println("Name: "+jsonArray.getJSONObject(j).get("username").toString())

                                isFriend = true

                                val mapFragment = supportFragmentManager
                                    .findFragmentById(R.id.mapView) as SupportMapFragment
                                mapFragment.getMapAsync(this)
                                    break




                            }

                        }
                    }
                    if(!isFriend)
                    {

                            textView.text = "To nie jest Twoj znajomy"
                        
                    }
                }

            })


    }
    fun setLocation(lat:Double, lng:Double){
        val location = LatLng(lat, lng)
        mMap.addMarker(MarkerOptions().position(location).title("Ostatnia lokalizacja twojego znajomego"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }


}

