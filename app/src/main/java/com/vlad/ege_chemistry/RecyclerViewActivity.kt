package com.vlad.ege_chemistry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.vlad.ege_chemistry.databinding.ActivityRecyclerviewBinding

class RecyclerViewActivity : AppCompatActivity() {

    private val recyclerViewItems: ArrayList<String> = ArrayList<String>()
    private val TAG = "LogRecyclerViewActivity"
    private var userSelectedMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = DataBindingUtil.setContentView<ActivityRecyclerviewBinding>(this, R.layout.activity_recyclerview)
        userSelectedMode = intent.getStringExtra("userSelectedMode").toString()

        // Сохраняем значение userSelectedMode в SharedPreferences
        val sharedPref = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("userSelectedMode", userSelectedMode)
        editor.apply()
        var columnCount = 1
        if(userSelectedMode == "selectTrialVariantExercise"){
            columnCount = 4
        }

        Log.d(TAG,userSelectedMode)
        inflateRecyclerViewItems()

        val adapter = RecyclerViewAdapter(recyclerViewItems,this)
        val layoutManager = GridLayoutManager(this,columnCount)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun inflateRecyclerViewItems(){
        Log.d(TAG,userSelectedMode)
        when(userSelectedMode){
            "teory" -> {
               val count = resources.getInteger(R.integer.teory_count)
                var textResourceId: Int
                for (i in 1..count){
                    textResourceId = resources.getIdentifier("teory$i","string",packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }
            "test" -> {
                val count = resources.getInteger(R.integer.test_count)
                var textResourceId: Int
                for (i in 1..count){
                    textResourceId = resources.getIdentifier("test_name$i","string",packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }
            "trialVariants" -> {
                val count = resources.getInteger(R.integer.trial_variants_count)
                var textResourceId: Int
                for (i in 1..count){
                    textResourceId = resources.getIdentifier("prName$i","string",packageName)
                    recyclerViewItems.add(resources.getString(textResourceId))
                }
            }
            "selectTrialVariantExercise" -> {
                for (i in 1..29){
                    recyclerViewItems.add(i.toString())
                }
                recyclerViewItems.add("Проверка")
                recyclerViewItems.add("Удалить ответы")
        }else -> {
            Log.e(TAG,"ERROR")
            }
        }
    }
    fun goToActivity(position: Int){
        // Получаем значение userSelectedMode из SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val userSelectedMode = sharedPref.getString("userSelectedMode", "").toString()
        Log.d(TAG, userSelectedMode)

        var intent = Intent()
        when(userSelectedMode){
            "teory"->{
                intent = Intent(this,TeoryActivity::class.java)
            }
            "test"->{
                intent = Intent(this,TestActivity::class.java)
            }
            "trialVariants"->{
                intent = Intent(this,RecyclerViewActivity::class.java)
                intent.putExtra("userSelectedMode","selectTrialVariantExercise")
                val editor = sharedPref.edit()
                editor.putString("prNumber",(position+1).toString())
                editor.apply()
            }
            "selectTrialVariantExercise"->{
                when (position) {
                    29 -> {
                        TrialVariantsActivity().checkAnswers()
                    }
                    30 -> {
                        TrialVariantsActivity().clearAnswers()
                    }
                    else -> {
                        intent = Intent(this,TrialVariantsActivity::class.java)
                    }
                }
            }
            else ->{
                Log.e(TAG,"ERROR!")
            }
        }
        intent.putExtra("position",position+1)
        startActivity(intent)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

