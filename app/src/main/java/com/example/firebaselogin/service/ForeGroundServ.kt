package com.example.firebaselogin.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.example.firebaselogin.activitys.HomePageActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.reflect.KFunction1

class ForeGroundServ() : Service() {
    lateinit var dbRef: DatabaseReference
    private lateinit var fireBaseAuth: FirebaseAuth
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createChanel()

    }

    val CHANNEL_ID:String = "Notification"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fireBaseAuth=FirebaseAuth.getInstance()
        val s = fireBaseAuth.currentUser?.email.toString()
        val beforeTheMark = s.split("@")
        val afterTheMark = beforeTheMark[1].split(".")
        dbRef =FirebaseDatabase.getInstance().getReference()
            .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")
        var tread = Workerthread(this,fireBaseAuth,dbRef)
        tread.start()
        var tr=NotifyThread(::NotificationUpdate)
        tr.start()
        var lg = LogOutThread()
        lg.start()
        return super.onStartCommand(intent, flags, startId)
    }

    private class Workerthread(ctx: Context, fireBaseAuth: FirebaseAuth, dbRef: DatabaseReference) : Thread() {
        var i: Int = 0
        var ctX=ctx
        var fbAuth=fireBaseAuth
        var fbDb = dbRef

        override fun run() {
            Log.i("Hello","Im in the run")
            var totalIncome = 0
            fbDb.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        totalIncome=0
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(IncomeClass::class.java)

                            // if user date !=12
                            if (user != null) {
                                    totalIncome+=user.incomeAcmount!!.toInt()
                            }
                        }
                        var inte = Intent()
                        inte.setAction("Counter")
                        Log.i("Hello","IM IN FB")
                        inte.putExtra("Counter",totalIncome.toString())
                        ctX.sendBroadcast(inte)

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i("Hello", "Im cancelled")
                }

            })


        }
    }

    private class NotifyThread( notUpdatte: KFunction1<Int, Unit>) : Thread() {
        var i= 0
        var notUp=notUpdatte
        override fun run() {
            while(i<100){
                Log.i("Hello","Im in the run")
                var totalIncome = 0
                i++
                Thread.sleep(5000)
                notUp(i)
            }

        }
    }

    private class LogOutThread():Thread(){
        val i=0
        override fun run() {
            while(i<100){
                Thread.sleep(500)
            }

        }
    }
    // notify the user that he need not to forget put all of his stats daily
    @RequiresApi(Build.VERSION_CODES.O)
    fun NotificationUpdate(i:Int){
        var isNotifyed =  android.preference.PreferenceManager.getDefaultSharedPreferences(this).getBoolean("NotificationCheck",true)
        if(isNotifyed){
            val notificationIntent = Intent(this,HomePageActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)
            val notification = Notification
                .Builder(this,CHANNEL_ID)
                .setContentTitle("Expense Tracker")
                .setContentText("Keep statss updated"+i.toString())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .build()
            startForeground(1,notification)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun createChanel(){
        var nc:NotificationChannel = NotificationChannel(CHANNEL_ID,"My Counter",NotificationManager.IMPORTANCE_DEFAULT)
        var nM:NotificationManager = getSystemService(NotificationManager::class.java)
        nM.createNotificationChannel(nc)
    }


}