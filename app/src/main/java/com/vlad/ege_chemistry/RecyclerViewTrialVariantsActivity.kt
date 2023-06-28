package com.vlad.ege_chemistry

import android.annotation.SuppressLint
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
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.vlad.ege_chemistry.databinding.ActivityRecyclerViewTrialVariantsBinding
import com.vlad.ege_chemistry.databinding.ActivityRecyclerviewBinding

class RecyclerViewTrialVariantsActivity : AppCompatActivity() {

    private val recyclerViewItems: ArrayList<String> = ArrayList()
    private var filledAnswers: ArrayList<String> = ArrayList()
    private var checkedAnswers: ArrayList<Boolean> = ArrayList()
    private var isAnswersChecked = false
    lateinit var binding: ActivityRecyclerViewTrialVariantsBinding
    private lateinit var adapter:RecyclerViewTrialVariantsAdapter
    private val TAG = "LogRecyclerTrialVariantsViewActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Выберите номер задания"

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recycler_view_trial_variants)
        createRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun createRecyclerView() {
        inflateRecyclerViewItems()
        filledAnswers = getAnswers()
        adapter = RecyclerViewTrialVariantsAdapter(recyclerViewItems, this,
            filledAnswers,isAnswersChecked,checkedAnswers, this)
        val layoutManager = GridLayoutManager(this, 4)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        adapter.notifyDataSetChanged()
    }
    private fun getAnswers():ArrayList<String> {
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val string = sharedPref.getString("answers", "").toString()
        Log.d(TAG, string)
        val userRawAnswers = string.split("/").drop(1)
        val userAnswers = ArrayList<String>()
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
        return userAnswers
    }

    private fun inflateRecyclerViewItems() {
            recyclerViewItems.clear()
        for (i in 0 until 29) {
            recyclerViewItems.add((i + 1).toString())
        }
                recyclerViewItems.add("Проверка")
                recyclerViewItems.add("Удалить ответы")
            }

    fun goToActivity(position: Int,answerText: String) {
        val intent: Intent
        when (position) {
            29 -> {
                checkAnswers()
            }
            30 -> {
                clearAnswers()
            }
            else -> {
                intent = Intent(this, TrialVariantsActivity::class.java)
                intent.putExtra("position", position + 1)
                intent.putExtra("answerText",answerText)
                startActivity(intent)
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
                intent = Intent(this, RecyclerViewActivity::class.java)
                intent.putExtra("userSelectedMode","trialVariants")
                startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkAnswers() {
        isAnswersChecked = true
        checkedAnswers.clear()
        val userAnswers = getAnswers()
        val textResourceId = resources.getIdentifier("pr1_1_answers", "string", packageName)
        val correctAnswers = resources.getString(textResourceId).split(":")
        var quantityOfCorrectAnswers = 0
        for (i in correctAnswers.indices) {
            if (correctAnswers[i] == userAnswers[i]) {
                quantityOfCorrectAnswers++
                checkedAnswers.add(true)
            }
            else{
                checkedAnswers.add(false)
            }
        }
        Log.d(TAG, quantityOfCorrectAnswers.toString())
        saveToSharePref("pr1_1_res",quantityOfCorrectAnswers.toString())
        popup(quantityOfCorrectAnswers)
        createRecyclerView()
    }
    private fun clearAnswers() {
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.remove("answers")
        editor.apply()
        isAnswersChecked = false
        createRecyclerView()
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
    private fun saveToSharePref(name:String,text:String){
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(name, text)
        editor.apply()
    }
}