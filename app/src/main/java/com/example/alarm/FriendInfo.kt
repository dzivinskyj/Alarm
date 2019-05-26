package com.example.alarm

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import kotlinx.android.synthetic.main.activity_change_alarm.*
import org.json.JSONObject


class FriendInfo : AppCompatActivity(),OnMapReadyCallback {
    var isFriend  : Boolean = false

    var username = ""

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_info)
        mapView.view!!.visibility = View.GONE
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
        isFriend = false
        findFollowers(username)






    }

    override fun onResume() {
        super.onResume()
        isFriend = false
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val query = ParseUser.getQuery()
        query.whereEqualTo("username", username)
        query.getFirstInBackground{usr, e ->
            if(e == null){
                val query2 = ParseQuery.getQuery<ParseObject>("Alarms")

                query2.whereEqualTo("UserId", usr.objectId)

                var params = HashMap<String, String>()

                params["user2"] = ParseUser.getCurrentUser().objectId
                params["user1"] = usr.objectId

                ParseCloud.callFunctionInBackground("findFriendship", params,
                    FunctionCallback<HashMap<Any,Any>> { friendship, e ->
                        if(e==null){
                            val query = ParseQuery.getQuery<ParseObject>("Friends")
                            var json = JSONObject(friendship.toString())
                            var friendshipId = json.get("id").toString()
                            System.out.println("friendshipId "+friendshipId)
                            // Retrieve the object by id
                            query.getInBackground(friendshipId) { _, e ->
                                query2.getFirstInBackground { obj, e ->
                                    if (e == null) {
                                        var loc = obj.get("LastLocation").toString().split(", ")
                                        setLocation(loc[0].toDouble(), loc[1].toDouble())
                                        mapView.view!!.visibility = View.VISIBLE
                                    }

                                }
                            }
                        }
                    })


            }
        }


    }

    fun findFollowers(name : String) {
        val parametersForFollowers = HashMap<String, String>()
        parametersForFollowers.put("user", ParseUser.getCurrentUser().objectId.toString())
        //Toast.makeText(this, "Lol", Toast.LENGTH_SHORT).show()
        ParseCloud.callFunctionInBackground("getFollowers", parametersForFollowers,
            FunctionCallback<ArrayList<Any>> { followers, e ->
                if (e == null) {
                    //Toast.makeText(this, "Yay", Toast.LENGTH_SHORT).show()
                    for (i in 0 until followers.size) {
                        var jsonArray = JSONArray(followers.toString())
                        for (j in 0 until jsonArray.length()) {
                            if(jsonArray.getJSONObject(j).get("username").toString().equals(name))
                            {
                                System.out.println("Name: "+jsonArray.getJSONObject(j).get("username").toString())

                                isFriend = true

                                textView.text = "Ostatnia zapisana lokalizacja"
                                val mapFragment = supportFragmentManager
                                    .findFragmentById(R.id.mapView) as SupportMapFragment
                                mapFragment.getMapAsync(this)
                                    break

                            }

                        }
                    }
                    if(!isFriend)
                    {

                        textView.text = "Dodaj znajomego"
                        textView.setOnClickListener {
                        addFollower(ParseUser.getCurrentUser().objectId.toString(), username )

                        }

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


    fun addFollower(user1 : String, user2 : String) {
        val parametersForFollowers = HashMap<String, String>()
        var usernameOfFollower = ""
        parametersForFollowers.put("user1", ParseUser.getCurrentUser().objectId.toString())
        val query = ParseUser.getQuery()
        query.whereEqualTo("username", user2)
        query.findInBackground { users, e ->
            if (e == null) {
                // The query was successful, returns the users that matches
                // the criterias.
                for (user in users) {
                    if(user.objectId != "")
                    {
                        usernameOfFollower = user.objectId.toString()
                        System.out.println( "USER2"+ usernameOfFollower)
                    }

                    println(user.username)
                }
                parametersForFollowers.put("user2", usernameOfFollower)

                ParseCloud.callFunctionInBackground("addFollower", parametersForFollowers,
                    FunctionCallback<HashMap<Any, Any>> { followers, e ->
                        if (e == null) {
                            Toast.makeText(this@FriendInfo, "Użytkownik "+user2 + " dodany do znajomych! ", Toast.LENGTH_LONG).show()
                            finish()
                            startActivity(getIntent())


                        }
                        else
                        {
                            Toast.makeText(this@FriendInfo, "Oh no: " + e.toString(), Toast.LENGTH_LONG).show()

                        }
                    })
            } else {
                // Something went wrong.
            }
        }

    }
}

