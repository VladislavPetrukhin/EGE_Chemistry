package com.vlad.ege_chemistry.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.vlad.ege_chemistry.R

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            Log.d("NotifyLog", "permission: $isGranted")
        }
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
//            if(value == "light"){
//                switchTheme(requireContext(),false)
//            }else if (value == "dark"){
//                switchTheme(requireContext(),true)
//            }
        }else if(preferences.key.toString() == "pref_key_notifications"){
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            sharedPreferences.edit().putBoolean(key, preferences.isChecked).apply()
            if(preferences.isChecked){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notifySetup()
                }
        }
        }
    }
//    private fun switchTheme(context: Context, isDarkMode: Boolean) {
//        val themeId = if (isDarkMode) {
//            R.style.Theme_Dark_EGE_Chemistry
//        } else {
//            R.style.Theme_Light_EGE_Chemistry
//        }
//
//        context.setTheme(themeId)
//        val intent = Intent(context, MainActivity::class.java)
//        context.startActivity(intent)
//    }
    private fun notifySetup(){
    if(checkNotificationPermission(requireContext())){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }else{
        requestNotificationPermission(this,123)
    }
}
    private fun checkNotificationPermission(context: Context): Boolean {
        // Проверяем, разрешено ли отправлять уведомления
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    private fun requestNotificationPermission(activity: SettingsFragment, requestCode: Int) {
        // Запрашиваем разрешение на отправку уведомлений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, "com.vlad.ege_chemistry")
            activity.startActivityForResult(intent, requestCode)
        } else {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", "com.vlad.ege_chemistry", null)
            intent.data = uri
            activity.startActivityForResult(intent, requestCode)
        }
    }
}