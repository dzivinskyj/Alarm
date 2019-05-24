package com.example.alarm

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseUser


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Parse.initialize( Parse.Configuration.Builder(this)
            .applicationId(getString(R.string.back4app_app_id))
            // if defined
            .clientKey(getString(R.string.back4app_client_key))
            .server(getString(R.string.back4app_server_url))
            .build()
        )

        val channels = ArrayList<String>()
        //channels.add("JxAZ6fqOCN")
        val installation = ParseInstallation.getCurrentInstallation()
        installation.put("GCMSenderId", "144705776443")
        installation.saveInBackground()

        if(Build.VERSION.SDK_INT >= 26){
            var alarmChannel = NotificationChannel("alarmChannel", "Alarm", NotificationManager.IMPORTANCE_HIGH)
            var manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(alarmChannel)
        }
    }
}