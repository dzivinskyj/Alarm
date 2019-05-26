package com.example.alarm

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
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
import com.parse.ParseQuery
import com.parse.ParseObject



class FriendInfo : AppCompatActivity(),OnMapReadyCallback {
    var isFriend  : Boolean = false

    var username = ""

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        username = intent.getStringExtra("username")
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        //        ads:adUnitId="ca-app-pub-2251557349292337/2994514292"
        System.out.println(intent.getStringExtra("username"))
        //check if searched user is our friend
        System.out.println("HEJ"+findFollowers(intent.getStringExtra("username")).toString())
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        findFollowers(username)






    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val query = ParseUser.getQuery()
        query.whereEqualTo("username", username)
        query.getFirstInBackground{usr, e ->
            if(e == null){
                val query2 = ParseQuery.getQuery<ParseObject>("Alarms")

                query2.whereEqualTo("UserId", usr.objectId)

                query2.getFirstInBackground{obj,e ->
                    if(e==null){
                        var loc = obj.get("LastLocation").toString().split(", ")
                        setLocation(loc[0].toDouble(),loc[1].toDouble())
                    }

                }
            }
        }


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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
    }


}

