package com.vlad.ege_chemistry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.databinding.ActivityTeoryBinding

class TeoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityTeoryBinding>(this, R.layout.activity_teory)
        var position = intent.getIntExtra("position",1).toString()
        var textResourceId = resources.getIdentifier("text$position","string",packageName)
        binding.teoryTextView.text = resources.getString(textResourceId)
    }
}