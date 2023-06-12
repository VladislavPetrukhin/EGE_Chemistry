package com.vlad.ege_chemistry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.vlad.ege_chemistry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.buttonTeory.setOnClickListener {
            val intent = Intent(applicationContext,RecyclerViewActivity::class.java)
            intent.putExtra("type","teory")
            startActivity(intent)
        }
        binding.buttonTest.setOnClickListener {
            val intent = Intent(applicationContext,RecyclerViewActivity::class.java)
            intent.putExtra("type","test")
            startActivity(intent)
        }
        binding.buttonTrialVariants.setOnClickListener {
            val intent = Intent(applicationContext,RecyclerViewActivity::class.java)
            intent.putExtra("type","trialVariants")
            startActivity(intent)
        }

    }
}