package com.example.firebaselogin.fragments

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.example.firebaselogin.adapters.recycAdapter
import com.example.firebaselogin.utils.vibe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class view_by_table_fragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    lateinit var dbRef: DatabaseReference
    lateinit var sp: SharedPreferences
    var day = 0
    var month = 0
    var year = 0
    var saveday = 0
    var savemonth = 0
    var saveyear = 0
    lateinit var totalInc: TextView
    lateinit var totalCount: TextView
    lateinit var listOfTrans: ArrayList<IncomeClass>
    lateinit var adapter: recycAdapter
    var isVibrate = false
    lateinit var translateAnimation: Animation
    lateinit var btnChooseDate : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        fireBaseAuth = FirebaseAuth.getInstance()
        setHasOptionsMenu(true)
        val rv = view.findViewById(R.id.userList) as RecyclerView

         btnChooseDate = view.findViewById(R.id.btnClickDate) as Button
        val s = fireBaseAuth.currentUser?.email.toString()
        val beforeTheMark = s.split("@")
        val afterTheMark = beforeTheMark[1].split(".")
        totalInc = view.findViewById(R.id.TotalBalance) as TextView
        totalCount = view.findViewById(R.id.totalcount) as TextView
        translateAnimation = AnimationUtils.loadAnimation(context, R.anim.views_animation)
        btnChooseDate.visibility = View.INVISIBLE
        sp = PreferenceManager.getDefaultSharedPreferences(context)
        dbRef = FirebaseDatabase.getInstance().getReference()
            .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")
        rv.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(requireContext());
        rv.layoutManager = mLayoutManager
        listOfTrans = arrayListOf<IncomeClass>()
        //
        //rv.adapter = adapter
        adapter = recycAdapter(listOfTrans)
        rv.adapter = adapter
        setValues("2021/11/26", adapter, listOfTrans, totalInc, totalCount)
        btnChooseDate.setOnClickListener {
            getDateTimeCalander()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = "Watch Transactions"
        return inflater.inflate(R.layout.fragment_view_by_table_fragment, container, false)
    }

    private fun setValues(
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
                    var counter = 0
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(IncomeClass::class.java)

                        // if user date !=12
                        if (user != null) {
                            if (user.dateOfIncome!! == date) {
                                listOfTrans.add(user)
                                totalIncome += user.incomeAcmount!!.toInt()
                                counter++
                            }
                        }
                    }
                    if (totalIncome >= 0)
                        totalInc.setTextColor(Color.parseColor("#43a22b"))
                    else
                        totalInc.setTextColor(Color.parseColor("#d60000"))
                    totalInc.setText(totalIncome.toString())
                    totalCount.setText(counter.toString())
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveday = dayOfMonth
        savemonth = month
        saveyear = year
        if (isVibrate) {
            val vib = vibe()
            context?.let { it1 -> vib.setVibration(it1) }
        }
        listOfTrans.clear()
        sp.edit().putString("Month", (savemonth + 1).toString()).commit()
        sp.edit().putString("Year", (saveyear).toString()).commit()
        val sendingDate =
            saveyear.toString() + "/" + (savemonth + 1).toString() + "/" + saveday.toString()
        setValues(sendingDate, adapter, listOfTrans, totalInc, totalCount)

    }

    private fun getDateTimeCalander() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onPause() {
        btnChooseDate.visibility = View.INVISIBLE
        super.onPause()
    }

    override fun onResume() {
        isVibrate = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean("Vibration", false)
        btnChooseDate.visibility = View.VISIBLE
        btnChooseDate.startAnimation(translateAnimation)
        super.onResume()
    }

}