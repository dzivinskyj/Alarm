package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class PopupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up)
        val player = MediaPlayer.create(
            this!!,
            Settings.System.DEFAULT_RINGTONE_URI
        )
        player.start()

        var builder = AlertDialog.Builder(this)
        builder.setTitle("Twój alarm bezpieczeństwa")
        builder.setMessage("Czy jesteś w niebiezpieczeństwie?")
        builder.setPositiveButton("POMOCY!", DialogInterface.OnClickListener { dialog, which ->
            player.stop()
        })
        builder.setNegativeButton("Wszystko ok!", DialogInterface.OnClickListener { dialog, which ->
            val intentend = Intent(this!!, AlarmReceiver::class.java)
            val pendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, intentend, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            player.stop()
        })
        val dialog = builder.create()
        dialog.show()
        dialog.setOnDismissListener(){
            finish()
        }
    }
}