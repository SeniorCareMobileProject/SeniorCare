package com.SeniorCareMobileProject.seniorcare.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootBroadcastReceiver : BroadcastReceiver() {

    val ACTION = "android.intent.action.BOOT_COMPLETED"
    override fun onReceive(context: Context, intent: Intent) {
        // BOOT_COMPLETED” start Service
        //TODO
//        if (intent.action == ACTION) {
//            val serviceIntent = Intent(context, BackgroundLocationService::class.java)
//            context.startService(serviceIntent)
//        }
    }
}