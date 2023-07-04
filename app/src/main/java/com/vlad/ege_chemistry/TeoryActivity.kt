package com.vlad.ege_chemistry

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.preference.PreferenceManager
import com.vlad.ege_chemistry.databinding.ActivityTeoryBinding

class TeoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Изучите теорию"

        val binding = DataBindingUtil.setContentView<ActivityTeoryBinding>(this, R.layout.activity_teory)

        val defaultSharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val fontSize = defaultSharedPref.getString("pref_key_font_size", resources.getInteger(R.integer.mediumTextSize).toString()).toString()
        binding.teoryTextView.textSize = fontSize.toFloat()

        var position = intent.getIntExtra("position",1).toString()
        var textResourceId = resources.getIdentifier("text$position","string",packageName)
        binding.teoryTextView.text = resources.getString(textResourceId)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}