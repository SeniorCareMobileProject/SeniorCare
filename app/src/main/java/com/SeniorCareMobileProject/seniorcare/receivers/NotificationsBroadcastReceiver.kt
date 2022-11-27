package com.SeniorCareMobileProject.seniorcare.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.R

class NotificationsBroadcastReceiver: BroadcastReceiver(){



    override fun onReceive(context: Context, intent: Intent) {
        var notificationId = intent.getIntExtra("NotificationId",0)
        var timeId = intent.getIntExtra("TimeId", 0)
        var title = intent.getStringExtra("VALUE")
        if (title != null) {
            Log.e(TAG,title)
        }
        showNotification(MyApplication.context, notificationId, timeId, title)
    }

    private fun showNotification(context: Context?, notificationId: Int, timeId: Int, title: String?) {

        createNotificationChannel(context)
        Log.d("Notification", "showing")

        var mBuilder = NotificationCompat.Builder(context!!, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText("Pamietaj o leku")
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val contentIntent =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                context,
                notificationId*3 + timeId,
                Intent(context, GeofenceBroadcastReceiver::class.java),
                PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                context, notificationId*3 + timeId,
                Intent(context, GeofenceBroadcastReceiver::class.java), 0
            )
        }

        mBuilder.setContentIntent(contentIntent)

        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(notificationId, mBuilder.build())
    }
    private fun createNotificationChannel(context: Context?) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}