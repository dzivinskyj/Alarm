package com.example.alarm

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.parse.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ParseInstallation.getCurrentInstallation().saveInBackground()

        if(ParseUser.getCurrentUser()!=null){
            var intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
    }

    fun goToRegister(view : View){
        var intent = Intent(this, Register::class.java)
        startActivity(intent)

    }

    fun singIn(view : View){
        var login = findViewById<EditText>(R.id.login).text.toString()
        var pass = findViewById<EditText>(R.id.password).text.toString()
        ParseUser.logInInBackground(login, pass) { parseUser, e ->
            if (parseUser != null) {
                //Parse.initialize(this as Parse.Configuration)
                val channels = ArrayList<String>()
                channels.add(ParseUser.getCurrentUser().objectId.toString())
                val installation = ParseInstallation.getCurrentInstallation()
                installation.remove("channels")
                installation.put("GCMSenderId", "144705776443")
                installation.put("channels", channels)
                installation.saveInBackground()
                alertDisplayer("Udało się zalogować!", "Witaj ponownie $login!")
            } else {
                ParseUser.logOut()
                Toast.makeText(this@MainActivity, "Zły login/hasło.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun alertDisplayer(title:String, message:String) {
        val builder = AlertDialog.Builder(this@MainActivity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
                // don't forget to change the line below with the names of your Activities
                val intent = Intent(this@MainActivity, Home::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        val ok = builder.create()
        ok.show()
    }
}
