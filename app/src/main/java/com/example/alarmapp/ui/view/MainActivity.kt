package com.example.alarmapp.ui.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.alarmapp.data.notification.AlarmReceiver
import com.example.alarmapp.databinding.ActivityMainBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var picker: MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNotificationChannel()
        bindViews()
    }

    private fun bindViews() {
        binding.selectTimeButton.setOnClickListener {
            showTimePicker()
        }

        binding.setAlarm.setOnClickListener {
            setAlarms()
        }

        binding.CancelAlarm.setOnClickListener {
            setCancelAlarms()
        }
    }

    private fun setCancelAlarms() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this, "Alarm canceled", Toast.LENGTH_SHORT).show()
    }

    private fun setAlarms() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "successfully", Toast.LENGTH_SHORT).show()
    }


    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12).setMinute(0).setTitleText("Select Alarm time ").build()

        picker.show(supportFragmentManager, "Android")
        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                val selectedTimeString = buildString {
                    append(String.format("%02d", picker.hour - 12))
                    append(":")
                    append(String.format("%02d", picker.minute))
                    append(" ")
                    append("PM")
                }
                binding.selectedTime.text = selectedTimeString
            } else {
                val selectedTimeString = buildString {
                    append(String.format("%02d", picker.hour))
                    append(" : ")
                    append(String.format("%02d", picker.minute))
                    append(" ")
                    append("AM")
                }
                binding.selectedTime.text = selectedTimeString
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
        }

    }

    private fun setNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "AndroidReminderChannel"
            val description = "Channel for Alarm Manager"
            val importance = IMPORTANCE_HIGH
            val channel = NotificationChannel("Android", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}