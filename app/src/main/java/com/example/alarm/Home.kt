package com.example.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.parse.*


class Home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<TextView>(R.id.textView).text = "Zalogowano: ${ParseUser.getCurrentUser().username}!"
    }

    fun test(view : View){
        var message = "Wiadomość: "
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
        }
    }
}
