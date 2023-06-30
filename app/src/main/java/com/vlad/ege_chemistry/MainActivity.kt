package com.vlad.ege_chemistry

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.vlad.ege_chemistry.databinding.ActivityMainBinding
import com.vlad.ege_chemistry.fragments.HelpFragment
import com.vlad.ege_chemistry.fragments.MainFragment
import com.vlad.ege_chemistry.fragments.MotivationFragment
import com.vlad.ege_chemistry.fragments.RulesFragment
import com.vlad.ege_chemistry.fragments.SettingsFragment
import com.vlad.ege_chemistry.fragments.StatisticsFragment
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "channelID"

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24) // Устанавливаем значок меню
        setupNavigationDrawer()

        val defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val notifyOn = defaultSharedPref.getBoolean("pref_key_notifications", true)
        if(notifyOn){
            setupDailyNotification()
        }

        replaceFragment(MainFragment())
        supportActionBar?.title = resources.getString(R.string.fragment_main_name)
    }
//    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1
//
//    private fun requestNotificationPermission() {
//        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            Manifest.permission.POST_NOTIFICATIONS
//        } else {
//            TODO("VERSION.SDK_INT < TIRAMISU")
//        }
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//            // Показать объяснение пользователю, почему нужно разрешение на уведомления
//            // Например, можно показать диалоговое окно с объяснением
//
//            // Ваш код для показа объяснения
//
//        } else {
//            // Запросить разрешение у пользователя
//            ActivityCompat.requestPermissions(this, arrayOf(permission), NOTIFICATION_PERMISSION_REQUEST_CODE)
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Разрешение на уведомления получено
//                // Вы можете продолжить отправку уведомлений
//
//                // Ваш код для обработки получения разрешения на уведомления
//
//            } else {
//                // Разрешение на уведомления отклонено
//                // Вы должны адаптировать вашу логику в соответствии с этим
//            }
//        }
//    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun setupDailyNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)


        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18) // Установите желаемое время (час)
        calendar.set(Calendar.MINUTE, 18) // Установите желаемое время (минута)
        calendar.set(Calendar.SECOND, 0) // Установите желаемое время (секунда)

        Log.d("NotifyLog","1")
        // Устанавливаем повторение каждый день
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun setupNavigationDrawer() {
        navView.setNavigationItemSelectedListener { menuItem ->
            // Обработка выбора элемента навигационного меню
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    replaceFragment(MainFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_main_name)
                }
                R.id.nav_item2 -> {
                    replaceFragment(RulesFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_rules_name)
                }
                R.id.nav_item3 -> {
                    replaceFragment(HelpFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_help_name)
                }
                R.id.nav_item4 -> {
                    replaceFragment(MotivationFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_motivation_name)
                }
                R.id.nav_item5 -> {
                    replaceFragment(StatisticsFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_statistics_name)
                }
                R.id.nav_item6 -> {
                    replaceFragment(SettingsFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_settings_name)
                }
            }
            // Закрываем Navigation Drawer после выбора элемента
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Обработка нажатия на значок меню в ActionBar (для открытия Navigation Drawer)
        if (item.itemId == android.R.id.home) {
            if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START)
            }else{
                drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}