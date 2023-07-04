package com.vlad.ege_chemistry

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vlad.ege_chemistry.databinding.ActivityMainBinding
import com.vlad.ege_chemistry.fragments.FeedbackFragment
import com.vlad.ege_chemistry.fragments.HelpFragment
import com.vlad.ege_chemistry.fragments.MainFragment
import com.vlad.ege_chemistry.fragments.MotivationFragment
import com.vlad.ege_chemistry.fragments.RulesFragment
import com.vlad.ege_chemistry.fragments.SettingsFragment
import com.vlad.ege_chemistry.fragments.StatisticsFragment
import java.util.Calendar
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private val NOTIFICATION_ID = 101
    private val CHANNEL_ID = "channelID"
    private var contentTitle = ""
    private var contentText = ""
    private var fragmnent = "MainFragment"

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            Log.d("NotifyLog", "permission: $isGranted")
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

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
            Log.d("NotifyLog","true")
            //inflateNotificationContent(applicationContext)
            //showNotification(applicationContext)
            setupDailyNotification()
        }

        replaceFragment(MainFragment())
        supportActionBar?.title = resources.getString(R.string.fragment_main_name)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("fragment", fragmnent) // Сохранение значения
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        when(savedInstanceState.getString("fragment")){ // Извлечение значения
            "MainFragment"->{
                replaceFragment(MainFragment())
            }
            "RulesFragment"->{
                replaceFragment(RulesFragment())
            }
            "HelpFragment"->{
                replaceFragment(HelpFragment())
            }
            "MotivationFragment"->{
                replaceFragment(MotivationFragment())
            }
            "StatisticsFragment"->{
                replaceFragment(StatisticsFragment())
            }
            "SettingsFragment"->{
                replaceFragment(SettingsFragment())
            }
            "FeedbackFragment"->{
                replaceFragment(FeedbackFragment())
            }
            else->{
                replaceFragment(MainFragment())
            }
        }
    }
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

        alarmManager.cancel(pendingIntent)

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 18) // Установите желаемое время (час)
        calendar.set(Calendar.MINUTE, 18) // Установите желаемое время (минута)
        calendar.set(Calendar.SECOND, 0) // Установите желаемое время (секунда)

        Log.d("NotifyLog","setupDailyNotification")
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
                    fragmnent = "MainFragment"
                    replaceFragment(MainFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_main_name)
                }
                R.id.nav_item2 -> {
                    fragmnent = "RulesFragment"
                    replaceFragment(RulesFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_rules_name)
                }
                R.id.nav_item3 -> {
                    fragmnent = "HelpFragment"
                    replaceFragment(HelpFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_help_name)
                }
                R.id.nav_item4 -> {
                    fragmnent = "MotivationFragment"
                    replaceFragment(MotivationFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_motivation_name)
                }
                R.id.nav_item5 -> {
                    fragmnent = "StatisticsFragment"
                    replaceFragment(StatisticsFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_statistics_name)
                }
                R.id.nav_item6 -> {
                    fragmnent = "SettingsFragment"
                    replaceFragment(SettingsFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_settings_name)
                }
                R.id.nav_item7 -> {
                    fragmnent = "FeedbackFragment"
                    replaceFragment(FeedbackFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_feedback_name)
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
        private fun showNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_round)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("MainActivityLog","not granted")
                return
            }
            Log.d("MainActivityLog","granted")
            notify(NOTIFICATION_ID, builder.build())
        }
    }
    private fun inflateNotificationContent(context: Context){
        val notificationCount = context.resources.getInteger(R.integer.motivation_notification_count)
        val number = Random.nextInt(1,notificationCount+1)
        var textResourceId = context.resources.getIdentifier(
            "notification_title$number","string",context.packageName)
        contentTitle = context.resources.getString(textResourceId)
        textResourceId = context.resources.getIdentifier(
            "notification_text$number","string",context.packageName)
        contentText = context.resources.getString(textResourceId)
    }
}