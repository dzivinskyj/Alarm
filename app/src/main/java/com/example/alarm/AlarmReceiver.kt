package com.example.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import java.util.*
import android.media.RingtoneManager
import android.app.AlarmManager






class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager =
            context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent_new = Intent(context, ChangeAlarm::class.java)
        intent_new.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent_new, PendingIntent.FLAG_UPDATE_CURRENT)

        if (intent != null) {
            val calendar: Calendar = intent.extras!!.getSerializable("calendar") as Calendar
            var different =
               calendar.time.time -    Calendar.getInstance().getTime().time

            if(different <= 0){

            }
                val secondsInMilli: Long = 1000
                val minutesInMilli = secondsInMilli * 60
                val hoursInMilli = minutesInMilli * 60
                val daysInMilli = hoursInMilli * 24

                val elapsedDays = different / daysInMilli
                different %= daysInMilli

                val elapsedHours = different / hoursInMilli
                different %= hoursInMilli

                val elapsedMinutes = different / minutesInMilli
                different %= minutesInMilli


                var text = "Twój alarm zadzwoni za "

                if (elapsedDays > 0)
                    text = text + elapsedDays + " dni "
                if (elapsedHours > 0) {
                    text += elapsedHours
                    text = if (elapsedHours == 1L)
                        text + "godzinę "
                    else {
                        if (elapsedHours in 2..4) {
                            text + " godziny "
                        } else {
                            text + " godzin "
                        }
                    }
                }
                if (elapsedMinutes > 0) {
                    if(elapsedDays > 0)
                        text+="i "
                    text += elapsedMinutes
                    text = if (elapsedMinutes == 1L)
                        text + " minutę "
                    else {
                        if (elapsedMinutes in 2..4) {
                            text + " minuty "
                        } else {
                            text + " minut"
                        }
                    }
                }

                    val builder = NotificationCompat.Builder(context)
                        .setContentTitle("Twój alarm bezpieczeństwa.")
                        .setContentText(text)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                        .setAutoCancel(true)

                    notificationManager.notify(0, builder.build())
        }
    }}


