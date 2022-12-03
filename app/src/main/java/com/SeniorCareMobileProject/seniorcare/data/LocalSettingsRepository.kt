package com.SeniorCareMobileProject.seniorcare.data

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class LocalSettingsRepository private constructor(application: Context) {

    companion object {
        private var INSTANCE: LocalSettingsRepository? = null
        fun getInstance(application: Context): LocalSettingsRepository {
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

    fun readUserFunction(): String? {
        return sharedPreferences.getString("User_function", "")
    }

    fun saveUserFunction(userFuntion: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("User_function", userFuntion).apply()
    }

    fun readSosNumbers(): String? {
        return sharedPreferences.getString("Sos_numbers", "")
    }

    fun saveSosNumbers(text: Any) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("Sos_numbers", text.toString()).apply()
    }

    fun readFallDetectionState(): Boolean {
        return sharedPreferences.getBoolean("Fall_detection", true)
    }

    fun saveFallDetectionState(boolean: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("Fall_detection", boolean).apply()
    }

    fun clearRepository() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear().apply()
    }
}