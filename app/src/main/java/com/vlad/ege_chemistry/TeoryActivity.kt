package com.vlad.ege_chemistry

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.databinding.ActivityTeoryBinding

class TeoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Изучите теорию"

        val binding = DataBindingUtil.setContentView<ActivityTeoryBinding>(this, R.layout.activity_teory)

        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val textSize = sharedPref.getInt("textSize",resources.getInteger(R.integer.mediumTextSize))
        binding.teoryTextView.textSize = textSize.toFloat()

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