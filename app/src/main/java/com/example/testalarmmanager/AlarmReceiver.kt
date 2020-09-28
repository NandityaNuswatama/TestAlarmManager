package com.example.testalarmmanager

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class AlarmReceiver : BroadcastReceiver()  {
    companion object{
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TIME = "time"
    }

    private val eightHours = 8*60*60*1000L
    private val twelveHours = 12*60*60*1000L
    private val requestCode = 3

    override fun onReceive(context: Context, intent: Intent) {
        val times = intent.getIntExtra(EXTRA_MESSAGE, requestCode)
        showAlarm(context, times)
    }

    fun set6Times(context: Context){
        set2Times(context)
        set3Times(context)
    }

    fun set3Times(context: Context){
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_TIME, requestCode)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, eightHours, pendingIntent)
    }

    fun set2Times(context: Context){
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, twelveHours, pendingIntent)
    }

    fun showAlarm(context: Context, times: Int){
        val channelId = "Channel_1"
        val channelName = "MedTrack channel"
        val vibrationPattern = longArrayOf(500, 0, 500, 0, 1000)

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, times, intent, 0)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
            .setContentTitle("Jangan lupa minum obat!")
            .setContentText("Semoga lekas sembuh, jangan terlalu banyak aktivitas dulu ya!")
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(vibrationPattern)
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.vibrationPattern = vibrationPattern
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(requestCode, notification)
    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = requestCode
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }
}