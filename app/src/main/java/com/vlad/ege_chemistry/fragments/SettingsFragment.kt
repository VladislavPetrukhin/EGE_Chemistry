package com.vlad.ege_chemistry.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.vlad.ege_chemistry.R

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    //    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
//            inflater, R.layout.fragment_settings, container, false
//        )
//        return binding.root
//    }
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val sharedPref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val notifications = defaultSharedPref.getBoolean("pref_key_notifications", true)
        val theme = defaultSharedPref.getString("pref_key_theme", "light")

        val preferenceScreen = preferenceScreen

        for (i in 0 until preferenceScreen.preferenceCount) {
            Log.d("PrefLog", i.toString())
            val preferences = preferenceScreen.getPreference(i)
            if (preferences !is CheckBoxPreference) {
                val value = sharedPref.getString(preferences.key, "").toString()
                setPreferenceLabel(preferences, value)
            }
        }
       // onSharedPreferenceChanged(preferenceScreen.sharedPreferences,"pref_key_theme")
      //  onSharedPreferenceChanged(preferenceScreen.sharedPreferences,"fontSize")
    }

    private fun setPreferenceLabel(preferences: Preference, value: String) {
        if (preferences is ListPreference) {
            val index = preferences.findIndexOfValue(value)
            if (index >= 0) {
                preferences.setSummary(preferences.entries[index])
            }
        }
    }

    override fun onSharedPreferenceChanged(sharefPreferences: SharedPreferences?, key: String?) {
        val preferences = findPreference<Preference>(key.toString()) as Preference
        if (preferences !is CheckBoxPreference) {
            val value = sharefPreferences?.getString(preferences.key, "").toString()
            setPreferenceLabel(preferences, value)
        }
    }
}