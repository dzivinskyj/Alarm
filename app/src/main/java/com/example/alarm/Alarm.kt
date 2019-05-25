package com.example.alarm

import android.app.*
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
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
import com.parse.ParseException
import kotlinx.android.synthetic.main.activity_alarm.*
import java.text.SimpleDateFormat
import java.util.*
import com.parse.ParseObject
import com.parse.ParseUser
import com.parse.SaveCallback
import com.parse.ParseQuery
import com.parse.GetCallback






class Alarm : Fragment() {
 override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?{

    return inflater!!.inflate(R.layout.activity_alarm, container, false)  }
    var lat : Double = 0.0
    var long : Double = 0.0

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

        descriptionView.setOnClickListener{
            hidden_edit_view.visibility = View.VISIBLE
            hidden_edit_view.hint = descriptionView.text
            descriptionView.visibility = View.INVISIBLE
        }
        hidden_edit_view.setOnEditorActionListener { textView, i, keyEvent ->

            if(keyEvent!=null) {
                descriptionView.text = hidden_edit_view.text

                hidden_edit_view.visibility = View.INVISIBLE
                descriptionView.visibility = View.VISIBLE

                if(hidden_edit_view.text.toString()=="")
                    descriptionView.text = "Uzupłenij opis"

            }
            true
        }

        cancel.setOnClickListener(){
             this.activity!!.finish()
        }
        destinationView.setOnClickListener(){
            var intent = Intent(this.context, PickLocation::class.java)
            //startActivity(intent)
            startActivityForResult(intent, 1);

        }
        saveAlarm.setOnClickListener {

            if (cal.time.time < Calendar.getInstance().getTime().time) {
                Toast.makeText(this.context, "Termin alarmu już minął", Toast.LENGTH_SHORT).show()
            } else {

                val userID = ParseUser.getCurrentUser().objectId.toString()

                val query = ParseQuery.getQuery<ParseObject>("Alarms")

                query.whereEqualTo("UserId", userID)

                query.getFirstInBackground { obj, e ->
                    if (e == null) {
                        obj.put("Description", descriptionView.text.toString())
                        obj.put("AlarmDate", cal.time)
                        obj.put("TargetLocation", destinationView.text.toString())
                        obj.put("LastLocation", "A string")
                        obj.put("Active", true)
                        obj.put("NotificationsSend", false)
                        // All other fields will remain the same
                        obj.saveInBackground()
                    } else {
                        val entity = ParseObject("Alarms")
                        entity.put("UserId", userID)
                        entity.put("Description", descriptionView.text.toString())
                        entity.put("AlarmDate", cal.time)
                        entity.put("TargetLocation", destinationView.text.toString())
                        entity.put("LastLocation", "A string")
                        // Saves the new object.
                        // Notice that the SaveCallback is totally optional!
                        entity.saveInBackground {
                            // Here you can handle errors, if thrown. Otherwise, "e" should be null
                        }
                    }
                }




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

                Toast.makeText(this.context, "Alarm został zapisany.", Toast.LENGTH_SHORT).show()
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
    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent ) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                lat = data.getDoubleExtra("lat", 0.0);
                long = data.getDoubleExtra("long", 0.0);
                destinationView.setText("lat: "+lat.toString()+" long: "+long.toString())
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }



}
