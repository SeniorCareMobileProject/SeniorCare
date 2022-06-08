package com.SeniorCareMobileProject.seniorcare.ui.widget

import android.Manifest
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.SeniorCareMobileProject.seniorcare.R

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

        var number: String? = "123456789"
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        remoteViews.setOnClickPendingIntent(R.id.button2, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
    }
}