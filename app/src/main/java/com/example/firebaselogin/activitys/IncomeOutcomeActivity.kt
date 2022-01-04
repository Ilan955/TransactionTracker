package com.example.firebaselogin.activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.example.firebaselogin.R
import com.example.firebaselogin.adapters.ViewPagerAdapter
import com.example.firebaselogin.databinding.ActivityIncomeOutcomeBinding
import com.example.firebaselogin.dialog.TransactionAppDialog
import com.example.firebaselogin.fragments.IncomeFragment
import com.example.firebaselogin.fragments.OutcomeFragment
import com.google.android.material.tabs.TabLayout

class IncomeOutcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIncomeOutcomeBinding
    lateinit var viewPag:ViewPager
    lateinit var tab:TabLayout.Tab
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityIncomeOutcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPag = findViewById(R.id.viewPager)
        setUpTabs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_file, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settingsBtn -> {

                val inten = Intent(this, settingsActivity::class.java)
                startActivity(inten)
            }
            R.id.item1 -> {
                val fragmentManager = supportFragmentManager
                val logOutDialog = TransactionAppDialog("Exit")
                logOutDialog.show(fragmentManager, "logOut")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(IncomeFragment(),"Income")
        adapter.addFragment(OutcomeFragment(),"Expense")
        viewPag.adapter=adapter
        binding.tabs.setupWithViewPager(viewPag)
        binding.tabs.getTabAt(0)!!.setIcon(R.drawable.ic_baseline_add_circle_24)
        binding.tabs.getTabAt(1)!!.setIcon(R.drawable.ic_baseline_remove_circle_24)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right)
    }



}