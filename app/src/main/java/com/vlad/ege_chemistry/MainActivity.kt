package com.vlad.ege_chemistry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.vlad.ege_chemistry.databinding.ActivityMainBinding
import com.vlad.ege_chemistry.fragments.HelpFragment
import com.vlad.ege_chemistry.fragments.MainFragment
import com.vlad.ege_chemistry.fragments.MotivationFragment
import com.vlad.ege_chemistry.fragments.RulesFragment
import com.vlad.ege_chemistry.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    private var isDrawerOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24) // Устанавливаем значок меню
        setupNavigationDrawer()

        replaceFragment(MainFragment())
        supportActionBar?.title = resources.getString(R.string.fragment_main_name)

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
                    replaceFragment(SettingsFragment())
                    supportActionBar?.title = resources.getString(R.string.fragment_settings_name)
                }
            }
            // Закрываем Navigation Drawer после выбора элемента
            isDrawerOpen = !isDrawerOpen
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
            if(isDrawerOpen){
                drawerLayout.closeDrawer(GravityCompat.START)
            }else{
                drawerLayout.openDrawer(GravityCompat.START)
            }
            isDrawerOpen = !isDrawerOpen
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}