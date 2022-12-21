package com.SeniorCareMobileProject.seniorcare.fallDetector

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import com.SeniorCareMobileProject.seniorcare.R

class AlarmPlayingUtil(private val context: Context) {
    fun createMediaPlayer(): MediaPlayer {
        val mediaPlayer: MediaPlayer = MediaPlayer()
        val afd: AssetFileDescriptor =
            context.resources.openRawResourceFd(R.raw.alarm)
        mediaPlayer.setDataSource(afd)
        afd.close()
        mediaPlayer.isLooping = true
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )
        //setAlarmToMaxVolume()
        mediaPlayer.prepare()
        return mediaPlayer
    }

    private fun setAlarmToMaxVolume() {
        val manager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val loudest = manager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
        manager.setStreamVolume(AudioManager.STREAM_ALARM, loudest, 0)
    }
}