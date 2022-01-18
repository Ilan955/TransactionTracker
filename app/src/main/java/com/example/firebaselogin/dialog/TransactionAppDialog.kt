package com.example.firebaselogin.dialog

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.example.firebaselogin.IncomeClass
import com.example.firebaselogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

class TransactionAppDialog(
    actionType: String,
    l: IncomeClass? = null,
) : DialogFragment() {
    private lateinit var fireBaseAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    private val disClass = l

    // ProgreDialog
    private lateinit var progressBar: ProgressDialog


    private val actionTypo = actionType
    lateinit var sp: SharedPreferences
    lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (actionTypo == "Exit") {
            rootView = inflater.inflate(R.layout.log_out_dialog, container, false)
            val noBtn = rootView.findViewById(R.id.noButton) as Button
            val yesBtn = rootView.findViewById(R.id.yesButton) as Button
            sp = PreferenceManager.getDefaultSharedPreferences(context)
            noBtn.setOnClickListener {
                dialog?.dismiss()

            }
            fireBaseAuth = FirebaseAuth.getInstance()
            yesBtn.setOnClickListener {
                fireBaseAuth.signOut()
                dialog?.dismiss()
                sp.edit().putBoolean("StayLoggedIn", false).commit()
                System.exit(0)
            }
        }


        else if (actionTypo == "EditView") {
            rootView = inflater.inflate(R.layout.change_view, container, false)
            val finishBtn = rootView.findViewById(R.id.submitBTN) as Button
            val cancelBTN = rootView.findViewById(R.id.cancelBTN) as Button
            fireBaseAuth = FirebaseAuth.getInstance()
            cancelBTN.setOnClickListener{
                dialog?.dismiss()
            }
            finishBtn.setOnClickListener {

                val newAmount = rootView.findViewById(R.id.amountET) as EditText
                val newDescription = rootView.findViewById(R.id.noteET) as EditText

                val s = fireBaseAuth.currentUser?.email.toString()

                // getting referance to the user who is connected right now
                val beforeTheMark = s.split("@")
                val afterTheMark = beforeTheMark[1].split(".")
                Log.i("Hello1",disClass?.key.toString())
                dbRef = FirebaseDatabase.getInstance().getReference()
                    .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")
                    .child(disClass?.key.toString())
                dbRef.child("incomeAcmount").setValue(newAmount.text.toString())
                dbRef.child("incomeDesc").setValue(newDescription.text.toString())
                dialog?.dismiss()
            }


        }
        else if(actionTypo=="removeUser"){
            fireBaseAuth = FirebaseAuth.getInstance()
            rootView = inflater.inflate(R.layout.remove_user_layout, container, false)
            val yesBtn = rootView.findViewById(R.id.yesButton) as Button
            val noBtn = rootView.findViewById(R.id.noButton) as Button

            noBtn.setOnClickListener{
                dialog?.dismiss()
            }
            yesBtn.setOnClickListener{
                val s = fireBaseAuth.currentUser?.email.toString()

                // getting referance to the user who is connected right now
                val beforeTheMark = s.split("@")
                val afterTheMark = beforeTheMark[1].split(".")

                dbRef = FirebaseDatabase.getInstance().getReference()
                    .child(beforeTheMark[0] + "@" + afterTheMark[0] + "/Transactions")
                    .child(disClass?.key.toString())

                dbRef.removeValue()
                dialog?.dismiss()
            }
        }
        else {
            rootView = inflater.inflate(R.layout.information_dialog, container, false)
            val okBtn = rootView.findViewById(R.id.okBtn) as Button
            okBtn.setOnClickListener {
                dialog?.dismiss()
            }
        }




        return rootView
    }


}