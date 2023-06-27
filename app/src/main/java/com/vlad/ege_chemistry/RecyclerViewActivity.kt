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
        val sharedPref: SharedPreferences =
            this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        // Сохраняем значение userSelectedMode в SharedPreferences
        val editor = sharedPref.edit()
        editor.putString("userSelectedMode", userSelectedMode)
        editor.apply()

        Log.d(TAG, userSelectedMode)
        createRecyclerView()
    }

    private fun createRecyclerView() {
        inflateRecyclerViewItems()

        var columnCount = 1
        if (userSelectedMode == "selectTrialVariantExercise") {
            columnCount = 4
        }

        val adapter = RecyclerViewAdapter(recyclerViewItems, this)
        val layoutManager = GridLayoutManager(this, columnCount)

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

            "selectTrialVariantExercise" -> {
                for (i in 1..29) {
                    recyclerViewItems.add(i.toString())
                }
                recyclerViewItems.add("Проверка")
                recyclerViewItems.add("Удалить ответы")
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
                intent = Intent(this, RecyclerViewActivity::class.java)
                intent.putExtra("userSelectedMode", "selectTrialVariantExercise")
                val editor = sharedPref.edit()
                editor.putString("prNumber", (position + 1).toString())
                editor.apply()
                clearAnswers()
            }

            "selectTrialVariantExercise" -> {
                when (position) {
                    29 -> {
                        checkAnswers()
                        return
                    }

                    30 -> {
                        clearAnswers()
                        return
                    }

                    else -> {
                        intent = Intent(this, TrialVariantsActivity::class.java)
                    }
                }
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
                val sharedPref: SharedPreferences =
                    this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("userSelectedMode", userSelectedMode)
                editor.apply()
                createRecyclerView()
            } else {
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkAnswers() {
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val string = sharedPref.getString("answers", "").toString()
        Log.d(TAG, string)
        var userRawAnswers = string.split("/").drop(1)
        var userAnswers = ArrayList<String>()
        for (i in 1..29) {
            userAnswers.add("")
        }
        var number: Int
        var answer: String
        for (i in userRawAnswers.indices) {
            number = userRawAnswers[i].split(":")[0].toInt()
            answer = userRawAnswers[i].split(":")[1]
            userAnswers[number - 1] = answer
        }
        val textResourceId = resources.getIdentifier("pr1_1_answers", "string", packageName)
        val correctAnswers = resources.getString(textResourceId).split(":")
        var quantityOfCorrectAnswers = 0
        for (i in correctAnswers.indices) {
            if (correctAnswers[i] == userAnswers[i]) {
                quantityOfCorrectAnswers++
            }
        }
        Log.d(TAG, quantityOfCorrectAnswers.toString())
        popup(quantityOfCorrectAnswers)
    }

    private fun clearAnswers() {
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("answers")
        editor.apply()
    }
    private fun popup(count:Int){
        // Создание объекта PopupWindow
        val popupView = layoutInflater.inflate(R.layout.popup_layout, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)

// Настройка параметров всплывающего окна
        popupWindow.isFocusable = true // Разрешить фокус на всплывающем окне
        popupWindow.isOutsideTouchable = true // Разрешить закрытие всплывающего окна при касании за его пределами

// Отображение всплывающего окна
        val parentView: View = findViewById(R.id.recyclerView)
        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0)

        val textview = popupView.findViewById<TextView>(R.id.popupTextView)
        val button = popupView.findViewById<Button>(R.id.popupButton)
        val string = "Правильных ответов: $count"
        textview.text = string
        button.setOnClickListener {
            popupWindow.dismiss()
        }

    }
}

