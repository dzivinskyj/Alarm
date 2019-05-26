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


class FriendInfo : AppCompatActivity(),OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        //        ads:adUnitId="ca-app-pub-2251557349292337/2994514292"
        System.out.println(intent.getStringExtra("username"))
        //check if searched user is our friend
        if(findFollowers(intent.getStringExtra("username")))
        {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.mapView) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        else
        {
            textView.text = "To nie jest Twoj znajomy"
        }






    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    fun findFollowers(username : String) : Boolean {
        val parametersForFollowers = HashMap<String, String>()
        var isFriend = false
        parametersForFollowers.put("user", ParseUser.getCurrentUser().objectId.toString())
        ParseCloud.callFunctionInBackground("getFollowers", parametersForFollowers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    for (i in 0 until followers.size) {
                        var jsonArray = JSONArray(followers.toString())
                        for (j in 0 until jsonArray.length()) {
                            if(jsonArray.getJSONObject(j).get("username").toString().equals(username))
                            {
                                isFriend = true
                            }

                        }
                    }
                }

            })
        return isFriend

    }


}

