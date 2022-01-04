package com.example.firebaselogin.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import androidx.core.content.ContextCompat.getSystemService

import android.os.Vibrator
import androidx.core.content.ContextCompat


class vibe {
    fun setVibration(ctx:Context){
        val vibrator = ctx?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }
}