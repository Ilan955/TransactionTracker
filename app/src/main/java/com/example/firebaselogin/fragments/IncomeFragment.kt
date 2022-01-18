package com.example.firebaselogin.fragments

import android.app.DatePickerDialog
import android.os.Build
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
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.example.firebaselogin.utils.vibe
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class IncomeFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    var day = 0
    var month = 0
    var year = 0
    private lateinit var actionBar: androidx.appcompat.app.ActionBar
    var saveday = 0
    var savemonth = 0
    var saveyear = 0
    var isPickedDate: Boolean = false
    var isVibrate = false
    private lateinit var fireBaseAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var dateInp: Button
    lateinit var Amount: EditText
    lateinit var Description: EditText
    lateinit var addBtn: Button
    lateinit var translateAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.title = "New Operation"


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        saveday = dayOfMonth
        savemonth = month + 1
        saveyear = year
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fireBaseAuth = FirebaseAuth.getInstance()
        dateInp = view.findViewById(R.id.DateET)
        Amount = view.findViewById(R.id.AmountET)
        Description = view.findViewById(R.id.DescriptionET)
        addBtn = view.findViewById(R.id.AddBtn)
        translateAnimation = AnimationUtils.loadAnimation(context, R.anim.views_animation)
        dateInp.visibility = View.INVISIBLE
        Amount.visibility = View.INVISIBLE
        Description.visibility = View.INVISIBLE
        addBtn.visibility = View.INVISIBLE


        addBtn.setOnClickListener {
            if (isVibrate) {
                val vib = vibe()
                context?.let { it1 -> vib.setVibration(it1) }
            }
            if (!isPickedDate)
                Toast.makeText(
                    context,
                    "You must choose the date you would like to enter the outcome...",
                    Toast.LENGTH_SHORT
                ).show()
            else {
                val s = fireBaseAuth.currentUser?.email.toString()
                val beforeTheMark = s.split("@")
                val afterTheMark = beforeTheMark[1].split(".")

                val sendingDate =
                    saveyear.toString() + "/" + savemonth.toString() + "/" + saveday.toString()
                var incomeAmount = Amount.text.toString().trim()
                var incomeDesc = Description.text.toString().trim()

                dbRef = FirebaseDatabase.getInstance().getReference()
                    .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")


                var newIncome = IncomeClass(
                    fireBaseAuth.currentUser?.email!!,
                    incomeDesc,
                    incomeAmount,
                    sendingDate,
                )
                Toast.makeText(
                    context,
                    "Succeed enter the new income to your account ",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }



        dateInp.setOnClickListener {
            getDateTimeCalander()
            isPickedDate = true
            context?.let { it1 -> DatePickerDialog(it1, this, year, month, day).show() }
        }


    }

    private fun getDateTimeCalander() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onPause() {
        dateInp.visibility = View.INVISIBLE
        Amount.visibility = View.INVISIBLE
        Description.visibility = View.INVISIBLE
        addBtn.visibility = View.INVISIBLE
        super.onPause()
    }

    override fun onResume() {
        isVibrate = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
            .getBoolean("Vibration", false)
        if (Amount != null) {
            dateInp.visibility = View.VISIBLE
            Amount.visibility = View.VISIBLE
            Description.visibility = View.VISIBLE
            addBtn.visibility = View.VISIBLE
            Amount.startAnimation(translateAnimation)
            Description.startAnimation(translateAnimation)
            addBtn.startAnimation(translateAnimation)
            dateInp.startAnimation(translateAnimation)
        }
        super.onResume()
    }

}