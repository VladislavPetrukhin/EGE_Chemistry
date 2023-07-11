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
        binding.teoryTextView.textSize = fontSize.toFloat()  //меняем размер шрифта

        val position = intent.getIntExtra("position",1).toString()

        var textResourceId = resources.getIdentifier("teory$position","string",packageName)
        supportActionBar?.title = resources.getText(textResourceId)

        textResourceId = resources.getIdentifier("text$position","string",packageName)
        binding.teoryTextView.text = resources.getText(textResourceId)  //выбираем текст в зависимости от позиции на которую нажали в recyclerview

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}