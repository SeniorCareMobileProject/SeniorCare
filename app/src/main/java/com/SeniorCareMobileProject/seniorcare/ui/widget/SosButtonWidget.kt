package com.SeniorCareMobileProject.seniorcare.ui.widget

import android.Manifest
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.SeniorCareMobileProject.seniorcare.MainActivity
import com.SeniorCareMobileProject.seniorcare.R
import com.SeniorCareMobileProject.seniorcare.WidgetActivity

/**
 * Implementation of App Widget functionality.
 */


class SosButtonWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(context: Context,
                             appWidgetManager: AppWidgetManager,
                             appWidgetId: Int){
    val remoteViews = RemoteViews(context.packageName, R.layout.sos_button_widget)


    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
        Toast.makeText(context, "Odmowa dostępu", Toast.LENGTH_SHORT).show()
    }
    else {

        val intent = Intent(context, WidgetActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 20, intent, FLAG_IMMUTABLE)



        remoteViews.setOnClickPendingIntent(R.id.button2, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }
}