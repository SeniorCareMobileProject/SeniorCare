package com.SeniorCareMobileProject.seniorcare

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: MyApplication? = null
            private set

        // or return instance.getApplicationContext();
        val context: Context?
            get() = instance
        // or return instance.getApplicationContext();
    }
}