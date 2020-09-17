package com.ismail.submission3.view.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ismail.submission3.AlarmReceiver
import com.ismail.submission3.R

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var LANGUAGE: String

    private lateinit var languagePreference : Preference
    private lateinit var isAlarmActive: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        init()
        setSummaries()
        alarmReceiver = AlarmReceiver()


    }

    private fun init() {
        ALARM = resources.getString(R.string.key_alarm)
        LANGUAGE = resources.getString(R.string.key_language)

        languagePreference = findPreference<Preference>(LANGUAGE) as Preference
        isAlarmActive = findPreference<SwitchPreference>(ALARM) as SwitchPreference
    }

    private fun setSummaries() {
        val sharedPreferences = preferenceManager.sharedPreferences
        isAlarmActive.isChecked = sharedPreferences.getBoolean(ALARM, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == ALARM) {
            isAlarmActive.isChecked = sharedPreferences.getBoolean(ALARM, false)

            if (isAlarmActive.isChecked) {
                val repeatMessage = getString(R.string.repeat_message)
                context?.let {
                    alarmReceiver.setRepeatingAlarm(
                        it, repeatMessage)
                }
            } else {
                context?.let { alarmReceiver.stopDailyReminder(it) }
            }

        }

    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return if (preference.key == LANGUAGE) {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            true
        } else false
    }

}
