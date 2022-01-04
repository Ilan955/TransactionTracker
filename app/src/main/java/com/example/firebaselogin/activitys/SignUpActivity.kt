package com.example.firebaselogin.activitys

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.firebaselogin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    // Action Bar
    private lateinit var actionBar: ActionBar

    private lateinit var progressBar: ProgressDialog

    private lateinit var fireBaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Sign up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        progressBar = ProgressDialog(this)
        progressBar.setTitle("Please wait")
        progressBar.setMessage("Creating account In..")
        progressBar.setCanceledOnTouchOutside(false)

        fireBaseAuth = FirebaseAuth.getInstance()

        binding.signUpButton.setOnClickListener {
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
        } else if (password.length < 6) {
            binding.passwordET.error = "Password is too short... must be at least 6 digits"

        } else {
            // data is valid, continue signup
            fireBaseSignUp()
        }

    }

    private fun fireBaseSignUp() {
        progressBar.show()
        // create account
        fireBaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressBar.dismiss()
                val fireBaseUser = fireBaseAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this, "Successful sign up with $email", Toast.LENGTH_SHORT).show()
                // open profile

                startActivity(Intent(this, HomePageActivity::class.java))
            }
            .addOnFailureListener { e ->
                progressBar.dismiss()
                Toast.makeText(this, "SigUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}