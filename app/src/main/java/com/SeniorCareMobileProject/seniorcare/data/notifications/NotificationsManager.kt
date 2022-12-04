package com.SeniorCareMobileProject.seniorcare.data.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.NotificationManagerCompat
import com.SeniorCareMobileProject.seniorcare.data.NotificationItem
import com.SeniorCareMobileProject.seniorcare.receivers.NotificationsBroadcastReceiver
import java.util.*

class NotificationsManager {

    private fun manageNotificationChannel(context: Context?, id: String) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, "channel_name", importance)
            channel.description = "channel desc"



            val notificationManager = NotificationManagerCompat.from(context!!)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleNotifications(context: Context, notificationItems: List<NotificationItem>){
        Log.e(TAG, "onStartCommand")
        Log.e(TAG,notificationItems.toString())
        //startTimer()

        for(i in 0 until notificationItems.size){
            for(j in 0 until notificationItems[i].timeList.size){
                Log.e(TAG,notificationItems[i].name)


                Log.e(TAG, Integer.parseInt(notificationItems[i].timeList[j].subSequence(0,2).toString()).toString() + " " + Integer.parseInt(notificationItems[i].timeList[j].subSequence(3,5).toString()).toString() )
                setAlarm(
                    context,
                    notificationItems,
                    Integer.parseInt(notificationItems[i].timeList[j].subSequence(0,2).toString()),
                    Integer.parseInt(notificationItems[i].timeList[j].subSequence(3,5).toString()),
                    notificationItems[i].interval,
                    i,
                    j
                )
            }

        }
    }

    fun setAlarm(context: Context, notificationItems: List<NotificationItem>, hour: Int, minute: Int, interval: String, notificationId: Int, timeId: Int){
        manageNotificationChannel(context, notificationId.toString())
        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
        Log.e(TAG,notificationItems[notificationId].name)
        val bundle = Bundle()
        bundle.putInt("NotificationId",notificationId)
        bundle.putString("Title",notificationItems[notificationId].name)
        bundle.putInt("TimeId",timeId)
        val intent = Intent(context, NotificationsBroadcastReceiver::class.java)
        intent.putExtras(bundle)

        val pendingIntent = PendingIntent.getBroadcast(context, notificationId*3 + timeId, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var alarmInterval = AlarmManager.INTERVAL_DAY
        if(interval.equals("Co 2 dni")){
            alarmInterval *= 2
        }
        if(interval.equals("Co tydzień")){
            alarmInterval *= 7
        }

        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.HOUR_OF_DAY) >= hour && get(Calendar.MINUTE)>=minute) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmInterval,
            pendingIntent
        )
    }

    fun cancelAllAlarms(context: Context, notificationItems: List<NotificationItem>){
        for(i in 0 until notificationItems.size){
            for(j in 0 until notificationItems[i].timeList.size){

                cancelAlarm(
                    context,
                    i,
                    j
                )
            }

        }
    }

    fun cancelAlarm(context: Context, notificationId: Int, timeId: Int){

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId*3+timeId)
        notificationManager.deleteNotificationChannel("$notificationId")
    }


}