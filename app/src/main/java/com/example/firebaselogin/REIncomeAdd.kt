package com.example.firebaselogin

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.firebaselogin.databinding.ActivityProfileBinding
import com.example.firebaselogin.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var binding : ActivityProfileBinding
private lateinit var actionBar: ActionBar
private lateinit var fireBaseAuth:FirebaseAuth


class ProfileActivity : AppCompatActivity() {

    lateinit var dbRef:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fireBaseAuth = FirebaseAuth.getInstance()
        binding.logOutButton.setOnClickListener{
          fireBaseAuth.signOut()
      }
        val s = fireBaseAuth.currentUser?.email.toString()
        val tmp = s.split("@")
        val tmp1 = tmp[1].split(".")
        Log.i("Hello",tmp1[1].toString())

        dbRef = FirebaseDatabase.getInstance().getReference().child(tmp[0]+"@"+tmp1[0]+"/outcomz")
        binding.insertionBtn.setOnClickListener{
            var incomeName = binding.incomeName.text.toString().trim()
            var incomeValue = binding.incomePrice.text.toString().trim()



            Toast.makeText(this,"Succedd enter the new income with $incomeName and with the $incomeValue", Toast.LENGTH_SHORT).show()
        }





    }
}