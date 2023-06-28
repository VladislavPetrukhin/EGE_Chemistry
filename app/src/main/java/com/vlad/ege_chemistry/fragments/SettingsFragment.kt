package com.vlad.ege_chemistry.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.R
import com.vlad.ege_chemistry.databinding.FragmentSettingsBinding

class SettingsFragment :  Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentSettingsBinding>(
            inflater, R.layout.fragment_settings, container, false
        )
        return binding.root
    }


//    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
//        setPreferencesFromResource(R.xml.preferences, rootKey)
//        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
//    }

//    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
//        when (key) {
//            "pref_key_notifications" -> {
//            }
//            "pref_key_font_size" -> {
//                updateFontSizePreference()
//            }
//        }
//    }
//    private fun updateFontSizePreference() {
//        val fontSizePreference = findPreference<ListPreference>("pref_key_font_size")
//        val selectedFontSize = fontSizePreference?.value
//        val sharedPref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//        val editor = sharedPref.edit()
//        when (selectedFontSize) {
//            "small" -> {
//                editor.putInt("textSize",resources.getInteger(R.integer.smallTextSize))
//            }
//            "medium" -> {
//                editor.putInt("textSize",resources.getInteger(R.integer.mediumTextSize))
//            }
//            "large" -> {
//                editor.putInt("textSize",resources.getInteger(R.integer.bigTextSize))
//            }
//        }
//        editor.apply()
//    }
}