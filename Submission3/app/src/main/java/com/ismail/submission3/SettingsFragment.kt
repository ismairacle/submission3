package com.ismail.submission3

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var isAlarmActive: SwitchPreference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        init()
        setSummaries()
        alarmReceiver = AlarmReceiver()
    }

    private fun setSummaries() {
        val sharedPreferences = preferenceManager.sharedPreferences
        isAlarmActive.isChecked = sharedPreferences.getBoolean(ALARM, false)
    }

    private fun init() {
        ALARM = resources.getString(R.string.alarm)
        isAlarmActive = findPreference<SwitchPreference>(ALARM) as SwitchPreference
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

                val repeatTime = "21:41"
                val repeatMessage = "It's time to find your favorite Github user on Our Apps"
                context?.let {
                    alarmReceiver.setRepeatingAlarm(
                        it,
                        repeatTime, repeatMessage)
                }
            } else {
                context?.let { alarmReceiver.stopDailyReminder(it) }
            }

        }
    }

}
