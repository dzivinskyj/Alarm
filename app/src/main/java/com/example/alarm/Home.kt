package com.example.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.parse.*
import org.json.JSONArray


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<TextView>(R.id.textView).text = "Zalogowano: ${ParseUser.getCurrentUser().username}!"
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
