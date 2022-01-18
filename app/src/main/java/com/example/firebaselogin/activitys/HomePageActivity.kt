package com.example.firebaselogin.activitys

import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.example.firebaselogin.adapters.recycAdapter
import com.example.firebaselogin.broadcast.BcReciveNET
import com.example.firebaselogin.databinding.ActivityHomePageBinding
import com.example.firebaselogin.dialog.TransactionAppDialog
import com.example.firebaselogin.service.ForeGroundServ
import com.example.firebaselogin.utils.vibe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomePageActivity : AppCompatActivity() {

    //binidng
    private lateinit var binding: ActivityHomePageBinding

    // auth
    private lateinit var fireBaseAuth: FirebaseAuth

    // ProgreDialog
    private lateinit var progressBar: ProgressDialog

    //Action bar
    private lateinit var actionBar: ActionBar
    var isVibrate = false
    private lateinit var rLayout: RelativeLayout

    //ref to DB
    lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        progressBar = ProgressDialog(this)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Fetching Data...")
        progressBar.setCanceledOnTouchOutside(false)
        progressBar.show()
        fireBaseAuth = FirebaseAuth.getInstance()
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        val s = fireBaseAuth.currentUser?.email.toString()

        // getting referance to the user who is connected right now
        val beforeTheMark = s.split("@")
        val afterTheMark = beforeTheMark[1].split(".")
        actionBar.title = "Welcome " + beforeTheMark[0]
        dbRef = FirebaseDatabase.getInstance().getReference()
            .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")

        // defining the Db reference and the  Adapter
        val rv = findViewById(R.id.LastTrans) as RecyclerView
        rv.setHasFixedSize(true)
        var listOfTrans: ArrayList<IncomeClass> = arrayListOf<IncomeClass>()
        val mLayoutManager = LinearLayoutManager(this)
        rv.layoutManager = mLayoutManager
        val adapter = recycAdapter(listOfTrans)
        rv.adapter = adapter

        // making a recycle view who will contain 4 elements represent the last 4 transactions
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var counter = 0
                    listOfTrans.clear()
                    for (userSnapshot in snapshot.children.reversed()) {
                        val user = userSnapshot.getValue(IncomeClass::class.java)
                        Log.i("Hello1",userSnapshot.key.toString())
                        // if user date !=12
                        if (user != null) {
                            if (counter < 4) {
                                user.key=userSnapshot.key.toString()
                                listOfTrans.add(user)
                                counter++
                            }
                        }
                    }
                    adapter.notifyDataSetChanged()
                    progressBar.hide()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        val sceondTxt = findViewById(R.id.NetProfitValue) as TextView

        // Self checking if the server already running, if so then wont start a new server
        if (!isMyServiceRunning(ForeGroundServ::class.java)) {
            var inten: Intent = Intent(this, ForeGroundServ::class.java)
            startService(inten)
        }


        var bc: BroadcastReceiver = BcReciveNET(sceondTxt)
        var intFilter: IntentFilter = IntentFilter()
        intFilter.addAction("Counter")
        registerReceiver(bc, intFilter)



        // Stating the Add Income/ Expense activity
        binding.IncomeBtn.setOnClickListener {
            if (isVibrate) {
                val vib = vibe()
                vib.setVibration(this)
            }
            val inten = Intent(this, IncomeOutcomeActivity::class.java)
            val intenService = Intent(this, ForeGroundServ::class.java)
            stopService(intenService)
            startActivity(inten)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }



        // Starting the View transaction activity
        binding.viewBtn.setOnClickListener {
            if (isVibrate) {
                val vib = vibe()
                vib.setVibration(this)
            }
            val inten = Intent(this, ViewingActivity::class.java)
            val intenService = Intent(this, ForeGroundServ::class.java)
            stopService(intenService)
            startActivity(inten)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_file, menu)
        return true
    }

    // When selecting the Exit the system will inflate the dialog class, if the settings will start the Setting activity having the prefrences
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsBtn -> {

                val inten = Intent(this, settingsActivity::class.java)
                startActivity(inten)
            }
            R.id.item1 -> {
                val fragmentManager = supportFragmentManager
                val logOutDialog = TransactionAppDialog("Exit")
                logOutDialog.isCancelable = false
                logOutDialog.show(fragmentManager, "logOut")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.className)) {
                return true
            }
        }
        return false

    }

    // Check weather the user want to vibrate for the button or not
    override fun onResume() {
        isVibrate = android.preference.PreferenceManager.getDefaultSharedPreferences(this)
            .getBoolean("Vibration", false)
        super.onResume()
    }


}