package com.SeniorCareMobileProject.seniorcare.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.SeniorCareMobileProject.seniorcare.MyApplication
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
import com.SeniorCareMobileProject.seniorcare.data.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class NotificationsBroadcastReceiver: BroadcastReceiver(){



    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val bundle = intent.extras
        val notificationId = bundle!!.getInt("NotificationId")
        val timeId = bundle.getInt("TimeId")
        val title = bundle.getString("Title")
        Log.e(TAG,"$title "+ "  + $timeId + $notificationId")

        showNotification(MyApplication.context, notificationId, timeId, title)
        val notificationItem = NotificationItem(
            name = title.toString(),
            timeList = mutableListOf(
                LocalDateTime.now().toLocalTime().format(
                DateTimeFormatter.ofPattern("HH:mm")))
        )
        Repository().saveLatestNotification(notificationItem)
    }

    private fun showNotification(context: Context?, notificationId: Int, timeId: Int, title: String?) {


        val mBuilder = NotificationCompat.Builder(context!!, "$notificationId")
            //.setSmallIcon(R.drawable.ic_circle_notifications)
            .setSmallIcon(R.drawable.ic_notification_smallicon)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.notifications_text,title))
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId*3+timeId)

        notificationManager.notify(notificationId*3+timeId, mBuilder.build())

    }


}