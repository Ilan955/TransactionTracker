package com.example.firebaselogin.activitys

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.preference.PreferenceManager
import com.example.firebaselogin.R
import com.example.firebaselogin.databinding.ActivityLoginBinding
import com.example.firebaselogin.dialog.TransactionAppDialog
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //binidng
    private lateinit var binding: ActivityLoginBinding

    //Action bar
    private lateinit var actionBar: ActionBar

    // ProgreDialog
    private lateinit var progressBar: ProgressDialog

    //FireBaseAuth
    private lateinit var fireBaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        fireBaseAuth = FirebaseAuth.getInstance()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        progressBar = ProgressDialog(this)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Loggin In...")
        progressBar.setCanceledOnTouchOutside(false)

        //init fireBase Auth


        checkUser()
        // handle click, begin Login

        binding.informationBtn.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val logOutDialog = TransactionAppDialog("Information")
            logOutDialog.isCancelable = false
            logOutDialog.show(fragmentManager, "information")
        }


        binding.noAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))

        }
        binding.LoginButton.setOnClickListener {
            //before logging in validate data
            validateData()
        }
    }

    private fun validateData() {

        email = binding.emailET.text.toString().trim()
        password = binding.passwordET.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid email format
            binding.emailET.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            //invalid password
            binding.passwordET.error = "Please enter password"
        } else {
            //data is valid, begin login

            fireBaseLogin()
        }
    }

    private fun fireBaseLogin() {
        progressBar.show()
        fireBaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                val fireBaseUser = fireBaseAuth.currentUser
                Toast.makeText(this, "Logged as $email", Toast.LENGTH_SHORT).show()
                val checkBox = this.findViewById(R.id.checkBox) as CheckBox

                // if the user checked the checkNBox then he will be saved and not been logged out.
                if (checkBox.isChecked)
                    sp.edit().putBoolean("StayLoggedIn", true).commit()
                else
                    sp.edit().putBoolean("StayLoggedIn", false).commit()
                startActivity(Intent(this, HomePageActivity::class.java))

                finish()
            }
            .addOnFailureListener { e ->
                //login faild.
                progressBar.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // if user is already logged in to profile activity
        //get current user
        val isClickd = sp.getBoolean("StayLoggedIn", false)
        if (!isClickd)
            return
        val fireBaseUser = fireBaseAuth.currentUser
        if (fireBaseUser != null) {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }


    }
}