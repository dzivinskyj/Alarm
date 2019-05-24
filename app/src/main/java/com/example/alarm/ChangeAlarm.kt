package com.example.alarm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_change_alarm.*
import android.app.PendingIntent
import android.app.AlarmManager
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

class ChangeAlarm : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_alarm)

        deleteActivity.setOnClickListener{
            finish()
        }
        editAlarm.setOnClickListener {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }
        deleteAlarm.setOnClickListener {

            val userID = ParseUser.getCurrentUser().objectId.toString()

            val query = ParseQuery.getQuery<ParseObject>("Alarms")

            query.whereEqualTo("UserId", userID)

            query.getFirstInBackground { obj, e ->
                if (e == null) {
                    obj.deleteInBackground()
                }
            }

            val intent1 = Intent(this, AlarmReceiver::class.java)
            val pendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            finish()

        }

    }


}