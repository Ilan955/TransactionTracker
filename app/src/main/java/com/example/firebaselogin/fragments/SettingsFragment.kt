package com.example.firebaselogin.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceFragmentCompat
import com.example.firebaselogin.R

class SettingsFragment : PreferenceFragment(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.prefernce, rootKey)
    }
}