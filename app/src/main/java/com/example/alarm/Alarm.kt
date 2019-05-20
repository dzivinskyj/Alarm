package com.example.alarm

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_alarm.*
import java.text.SimpleDateFormat
import java.util.*

class Alarm : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val date: TextView = findViewById(R.id.dateView)

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
            dateView.text = sdf.format(cal.time)
        }

        val time: TextView = findViewById(R.id.timeView)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
            timeView.text = sdf.format(cal.time)

        }


        time.setOnClickListener{
            TimePickerDialog(
                this, timeSetListener,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE), true
            ).show()

        }

        date.setOnClickListener {
            DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        cancel.setOnClickListener(){
            finish()
        }

        saveAlarm.setOnClickListener {

            if (cal.time.time < Calendar.getInstance().getTime().time) {
                Toast.makeText(this, "Termin alarmu już minął", Toast.LENGTH_SHORT).show()
            } else {

                val intent1 = Intent(this, AlarmReceiver::class.java)

                intent1.putExtra("calendar", cal)
                val pendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)


                val alarmManager: AlarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    (cal.timeInMillis - (2 * 3600 * 1000)),
                    200,
                    pendingIntent
                )
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()

        val sharedPref: SharedPreferences = this.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("czas", timeView.text as String)
        editor.putString("data", dateView.text as String)
        editor.putString("miejsce",destinationView.text as String)
        editor.putString("opis",descriptionView.text as String)

        editor.commit()
    }

    override fun onResume() {
        super.onResume()


        val prefs = this.getPreferences(Context.MODE_PRIVATE)
        if (prefs.getString("czas","HH:MM") != "HH:MM") {
            timeView.text= prefs.getString("czas","")
            dateView.text = prefs.getString("data","")
            destinationView.text = prefs.getString("miejsce","")
            descriptionView.text = prefs.getString("opis","")
        }
    }



}
