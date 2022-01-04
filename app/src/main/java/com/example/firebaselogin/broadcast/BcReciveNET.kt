package com.example.firebaselogin.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.example.firebaselogin.service.ForeGroundServ

class BcReciveNET(textToUpdate:TextView) : BroadcastReceiver() {
    var textView= textToUpdate
    override fun onReceive(context: Context?, intent: Intent?) {
        var amount:String = intent!!.getStringExtra("Counter")!!
        textView.setText(amount)
//        if (context != null) {
//            context.stopService(Intent(context.applicationContext,ForeGroundServ::class.java))
//        }
    }
}