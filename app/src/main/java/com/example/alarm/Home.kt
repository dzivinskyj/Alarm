package com.example.alarm

import android.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.parse.*
import org.json.JSONArray
import com.google.android.material.bottomnavigation.BottomNavigationView


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,Alarm()).commit()
        var bottomNavigationView : BottomNavigationView = findViewById(R.id.navigation)

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
                selectedFragment = Location()
            }

            supportFragmentManager.beginTransaction().replace(R.id.fragment_container,selectedFragment).commit()
            true
        }
    }

    fun test(view : View){
        val parameters = HashMap<String, String>()
        var channels = "JxAZ6fqOCN CH8txr6UJj"
        parameters.put("channels", channels)
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
