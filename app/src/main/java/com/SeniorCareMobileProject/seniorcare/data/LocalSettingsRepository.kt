package com.SeniorCareMobileProject.seniorcare.data

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class LocalSettingsRepository private constructor(application: Application) {

    companion object {
        private var INSTANCE: LocalSettingsRepository? = null
        fun getInstance(application: Application): LocalSettingsRepository {
            if (INSTANCE == null) {
                INSTANCE = LocalSettingsRepository(
                    application = application)
            }
            return INSTANCE as LocalSettingsRepository
        }
    }

    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        "Settings",
        MODE_PRIVATE)

    fun readSosNumbers(): String? {
        return sharedPreferences.getString("Sos_numbers", "")
    }

    fun saveSosNumbers(text: Any) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Sos_numbers", text.toString()).apply()
    }
}