package com.example.firebaselogin.dialog

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.example.firebaselogin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlin.system.exitProcess

class TransactionAppDialog(actionType: String) : DialogFragment() {
    private lateinit var fireBaseAuth: FirebaseAuth

    // ProgreDialog
    private lateinit var progressBar: ProgressDialog

    //Action bar
    private lateinit var actionBar: ActionBar
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
        } else {
            rootView = inflater.inflate(R.layout.information_dialog, container, false)
            val okBtn = rootView.findViewById(R.id.okBtn) as Button
            okBtn.setOnClickListener{
                dialog?.dismiss()
            }
        }




        return rootView
    }


}