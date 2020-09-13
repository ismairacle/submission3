package com.ismail.submission3.view.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ismail.submission3.R
import com.ismail.submission3.view.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, SettingsFragment()).commit()
    }

}