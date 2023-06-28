package com.vlad.ege_chemistry

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.vlad.ege_chemistry.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {

    private val recyclerViewItems: ArrayList<String> = ArrayList<String>()
    lateinit var binding: ActivityRecyclerviewBinding
    private val TAG = "LogRecyclerViewActivity"
    private var userSelectedMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recyclerview)
        userSelectedMode = intent.getStringExtra("userSelectedMode").toString()
        saveToSharePref("userSelectedMode",userSelectedMode)

        Log.d(TAG, userSelectedMode)
        createRecyclerView()
    }

    private fun createRecyclerView() {
        inflateRecyclerViewItems()

        val adapter = RecyclerViewAdapter(recyclerViewItems, this)
        val layoutManager = LinearLayoutManager(this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun inflateRecyclerViewItems() {
        Log.d(TAG, userSelectedMode)
        recyclerViewItems.clear()
        when (userSelectedMode) {
            "teory" -> {
                val count = resources.getInteger(R.integer.teory_count)
                var textResourceId: Int
                for (i in 1..count) {
                    textResourceId = resources.getIdentifier("teory$i", "string", packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }

            "test" -> {
                val count = resources.getInteger(R.integer.test_count)
                var textResourceId: Int
                for (i in 1..count) {
                    textResourceId = resources.getIdentifier("test_name$i", "string", packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }

            "trialVariants" -> {
                val count = resources.getInteger(R.integer.trial_variants_count)
                var textResourceId: Int
                for (i in 1..count) {
                    textResourceId = resources.getIdentifier("prName$i", "string", packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }
            else -> {
                Log.e(TAG, "ERROR")
            }
        }
    }

   fun goToActivity(position: Int) {
        // Получаем значение userSelectedMode из SharedPreferences
        val sharedPref: SharedPreferences =
            this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val userSelectedMode = sharedPref.getString("userSelectedMode", "").toString()
        Log.d(TAG, userSelectedMode)

        var intent = Intent()
        when (userSelectedMode) {
            "teory" -> {
                intent = Intent(this, TeoryActivity::class.java)
            }

            "test" -> {
                intent = Intent(this, TestActivity::class.java)
            }

            "trialVariants" -> {
                intent = Intent(this, RecyclerViewTrialVariantsActivity::class.java)
               // saveToSharePref("prNumber",(position + 1).toString())
                //clearAnswers()
            }
            else -> {
                Log.e(TAG, "ERROR!")
            }
        }
        intent.putExtra("position", position + 1)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (userSelectedMode == "selectTrialVariantExercise") {
                userSelectedMode = "trialVariants"
                saveToSharePref("userSelectedMode",userSelectedMode)
                createRecyclerView()
            } else {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun saveToSharePref(name:String,text:String){
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(name, text)
        editor.apply()
    }
}

