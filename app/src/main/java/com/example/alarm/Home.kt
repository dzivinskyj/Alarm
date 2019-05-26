package com.example.alarm

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.*
import org.json.JSONArray
import com.google.android.material.bottomnavigation.BottomNavigationView


class Home : AppCompatActivity() {

    override fun onBackPressed() {

    }
    class MapManager : OnMapReadyCallback, LocationListener {
        private lateinit var mMap: GoogleMap
        private var locationManager : LocationManager
        constructor(locationManager : LocationManager)
        {
            this.locationManager = locationManager
        }
        override fun onMapReady(p0: GoogleMap?) {
            //System.out.println("------- On map ready -------")
            //val sydney = LatLng(-34.0, 151.0)
            //p0!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            //p0!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap = p0!!
            getLocation()
        }

        override fun onLocationChanged(p0: Location?) {
            if(mMap != null) {
                val location = LatLng(p0!!.latitude, p0!!.longitude)
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(location).title("Twoja lokalizacja"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
            }
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Alarm()).commit()
        var bottomNavigationView : BottomNavigationView = findViewById(R.id.navigation)

        bottomNavigationView.selectedItemId = R.id.alarm

        bottomNavigationView.setOnNavigationItemSelectedListener {
            var selectedFragment : androidx.fragment.app.Fragment
            if(it.itemId == R.id.friendsList){

                selectedFragment = FriendsList()
            }
            else  if(it.itemId == R.id.alarm)
            {

                selectedFragment = Alarm()
            }
            else
            {
                //selectedFragment = Location(this)
                var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val supportMapFragment = com.google.android.gms.maps.SupportMapFragment()
                val mapManager = MapManager(locationManager)
                supportMapFragment.getMapAsync(mapManager)
                selectedFragment = supportMapFragment
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit()
            true
        }
    }

    fun test(view : View){
        val parametersForFollowers = HashMap<String, String>()
        parametersForFollowers.put("user", ParseUser.getCurrentUser().objectId.toString())
        ParseCloud.callFunctionInBackground("getFollowers", parametersForFollowers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    var channels = ""
                    for(i in 0 until followers.size) {
                        var jsonArray = JSONArray(followers.toString())
                        for(j in 0 until jsonArray.length() )
                        {
                            channels += jsonArray.getJSONObject(j).get("id").toString()+" "

                        }
                    }
                    System.out.println(channels)
                    val parameters = HashMap<String, String>()

                    parameters.put("channels", channels)
                    parameters.put("message", "Kuba śmierdzi")
                    parameters.put("title", "SEKRET")

                    ParseCloud.callFunctionInBackground("pushsample", parameters,
                        FunctionCallback<String> { followers, e ->
                            if (e == null) {
                                var message = "lol"
                                /*for(i in 0 until followers.size){
                                    message +=followers[i].toString()+";"
                                }*/
                                Toast.makeText(this@Home, message, Toast.LENGTH_LONG).show()
                            } else {
                                // Something went wrong
                                Toast.makeText(this@Home, "Oh no: " + e.toString(), Toast.LENGTH_LONG).show()
                            }
                        })
                } else {
                    // Something went wrong
                    Toast.makeText(this@Home, "Oh no: " + e.toString(), Toast.LENGTH_LONG).show()
                }
            })

        /*var message = "Wiadomość: "
        val query = ParseUser.getQuery()
        query.whereEqualTo("email", ParseUser.getCurrentUser().email)
        query.findInBackground { users, e ->
            if (e == null) {

                for (user in users) {
                   message += user.username
                }

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                // Something went wrong.
            }
        }*/
    }
}
