package com.wakeupgetapp.seniorcare.utils

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
import android.util.Log
import com.wakeupgetapp.seniorcare.data.LocalSettingsRepository
import java.lang.IllegalArgumentException

class SendSmsUtil() {
    fun sendMultipleSms(context: Context?, message: String) {
        if (context == null) return
        val smsManager = createSmsManager(context)
        val numberList = getNumbersList(context)
        if (numberList.isEmpty()) return
        numberList.forEach { sendSms(smsManager, it, message) }
    }

    fun sendOneMessage(context: Context?, message: String) {
        if (context == null) return
        val smsManager = createSmsManager(context)
        val numberList = getNumbersList(context)
        if (numberList.isEmpty()) return
        val number = numberList.first()
        sendSms(smsManager, number, message)
    }

    private fun createSmsManager(context: Context): SmsManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }
    }

    private fun getNumbersList(context: Context): List<String> {
        val localSettingsRepository = LocalSettingsRepository.getInstance(context)
        val numbers = localSettingsRepository.readSosNumbers()

        val numbersToList = numbers?.split(",")?.map { it.trim() }
        return if (numbersToList.isNullOrEmpty())
            emptyList()
        else numbersToList
    }

    private fun sendSms(smsManager: SmsManager, number: String, message: String) {

        Log.d("SMS DEBUG", "$number, $message")
        if (number.isEmpty() or message.isEmpty()) return
        try {
            smsManager.sendTextMessage(
                number,
                null,
                message,
                null,
                null
            )
        } catch (e: IllegalArgumentException) {
            throw e.fillInStackTrace()
        } finally {

        }
    }

}