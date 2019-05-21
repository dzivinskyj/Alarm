package com.example.alarm

import android.app.*
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_alarm.*
import java.text.SimpleDateFormat
import java.util.*

class Alarm : Fragment() {
 override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{

    return inflater!!.inflate(R.layout.activity_alarm, container, false)  }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val date: TextView = dateView

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
            dateView.text = sdf.format(cal.time)
        }

        val time: TextView = timeView

        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
            timeView.text = sdf.format(cal.time)

        }


        time.setOnClickListener{
            TimePickerDialog(
                this.context, timeSetListener,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE), true
            ).show()

        }

        date.setOnClickListener {
            DatePickerDialog(
                this.context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        cancel.setOnClickListener(){
             this.activity!!.finish()
        }

        saveAlarm.setOnClickListener {

            if (cal.time.time < Calendar.getInstance().getTime().time) {
                Toast.makeText(this.context, "Termin alarmu już minął", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this.context, "Alarm został zapisany.", Toast.LENGTH_SHORT).show()

                val intent1 = Intent(
                    this.context,
                    AlarmReceiver::class.java
                )

                intent1.putExtra("calendar", cal)

                val pendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(this.context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)


                val alarmManager: AlarmManager = this.context!!.getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    (cal.timeInMillis - (2 * 3600 * 1000)),
                    200,
                    pendingIntent
                )
            }
        }
    }



    override fun onPause() {
        super.onPause()

        val sharedPref: SharedPreferences = this.activity!!.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putString("czas", timeView.text as String)
        editor.putString("data", dateView.text as String)
        editor.putString("miejsce",destinationView.text as String)
        editor.putString("opis",descriptionView.text as String)

        editor.commit()
    }

    override fun onResume() {
        super.onResume()


        val prefs = this.activity!!.getPreferences(Context.MODE_PRIVATE)
        if (prefs.getString("czas","HH:MM") != "HH:MM") {
            timeView.text= prefs.getString("czas","")
            dateView.text = prefs.getString("data","")
            destinationView.text = prefs.getString("miejsce","")
            descriptionView.text = prefs.getString("opis","")
        }
    }



}
