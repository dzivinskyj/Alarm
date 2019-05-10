package com.example.alarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.parse.ParseInstallation

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ParseInstallation.getCurrentInstallation().saveInBackground()
    }

    fun goToRegister(view : View){
        var intent = Intent(this, Register::class.java)
        startActivity(intent)

    }
}
