package com.example.alarmapp.data.notification

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.alarmapp.R
import com.example.alarmapp.ui.view.DestinationActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, DestinationActivity::class.java)
        intent!!.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_IMMUTABLE)

        val builderNotification = NotificationCompat.Builder(context!!, "Android")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Android Alarm Manager").setContentText("You know i`m trying (:")
            .setAutoCancel(true).setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        when {
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            -> {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                return
            }
            else -> notificationManager.notify(123, builderNotification.build())
        }
    }
}