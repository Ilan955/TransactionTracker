package com.example.firebaselogin.activitys

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.example.firebaselogin.adapters.recycAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class viewTrans : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var fireBaseAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var dbRefInc: DatabaseReference
    var day = 0
    var month = 0
    var year = 0
    var saveday = 0
    var savemonth = 0
    var saveyear = 0

    // Action Bar
    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_trans)
        actionBar = supportActionBar!!
        actionBar.title = "Transactions"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)
        fireBaseAuth = FirebaseAuth.getInstance()
        val rv = findViewById(R.id.userList) as RecyclerView

        val btnChooseDate = findViewById(R.id.btnClickDate) as Button
        val s = fireBaseAuth.currentUser?.email.toString()
        val beforeTheMark = s.split("@")
        val afterTheMark = beforeTheMark[1].split(".")
        val totalInc = findViewById(R.id.TotalBalance) as TextView
        val totalCount = findViewById(R.id.totalcount) as TextView
        dbRef = FirebaseDatabase.getInstance().getReference()
            .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")
        rv.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this);
        rv.layoutManager = mLayoutManager
        var listOfTrans: ArrayList<IncomeClass> = arrayListOf<IncomeClass>()
        //
        //rv.adapter = adapter
        val adapter = recycAdapter(listOfTrans)
        rv.adapter = adapter

        btnChooseDate.setOnClickListener {
            getDateTimeCalander()
            DatePickerDialog(this, this, year, month, day).show()
        }


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveday = dayOfMonth
        savemonth = month
        saveyear = year
        Log.i(
            "DatePicker",
            "The year is " + saveyear + "The month is " + savemonth + "The day is " + saveday
        )
    }


    private fun getDateTimeCalander() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }


    fun setValues(

        date: String,
        adapter: recycAdapter,
        listOfTrans: ArrayList<IncomeClass>,
        totalInc: TextView,
        totalCount: TextView
    ) {
        this.dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
            var totalIncome = 0
                    var counter=0
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(IncomeClass::class.java)

                        // if user date !=12
                        if (user != null) {
                            if (user.dateOfIncome!! == date){
                                listOfTrans.add(user)
                                totalIncome+=user.incomeAcmount!!.toInt()
                                counter++
                            }
                        }
                    }
                    if(totalIncome>=0)
                        totalInc.setTextColor(Color.parseColor("#43a22b"))
                    else
                        totalInc.setTextColor(Color.parseColor("#d60000"))
                    totalInc.setText(totalIncome.toString())
                    totalCount.setText(counter.toString())
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("Hello", "Im cancelled")
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }

}