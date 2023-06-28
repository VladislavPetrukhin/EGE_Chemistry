package com.vlad.ege_chemistry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.databinding.ActivityTrialVariantsBinding

class TrialVariantsActivity : AppCompatActivity() {

    lateinit var binding: ActivityTrialVariantsBinding
    private val TAG = "TrialVariantsActivityLog"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val exercise = intent.getIntExtra("position", 1).toString()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_trial_variants)

        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val textSize = sharedPref.getInt("textSize",resources.getInteger(R.integer.mediumTextSize))
        binding.testTrialTextView.textSize = textSize.toFloat()

        inflateExercise(exercise)

        binding.testTrialAnswerButton.setOnClickListener {
            var answers = sharedPref.getString("answers", "")
            answers += "/" + exercise + ":" + binding.testTrialEditText.text.toString().trim()
            val editor = sharedPref.edit()
            editor.putString("answers", answers)
            editor.putString("userSelectedMode", "trialVariants")
            editor.apply()
            Log.d(TAG,answers.toString())
            intent = Intent(this, RecyclerViewActivity::class.java)
            intent.putExtra("userSelectedMode", "selectTrialVariantExercise")
            startActivity(intent)
        }
    }
    private fun inflateExercise(exercise: String) {
        val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val prNumber = sharedPref.getString("prNumber", "1")
        Log.d(TAG, prNumber.toString())
        Log.d(TAG, exercise.toString())
        val textResourceId =
            resources.getIdentifier("pr${prNumber}_$exercise", "string", packageName)
        binding.testTrialTextView.text = resources.getString(textResourceId)
        binding.testTrialEditText.setText("")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val sharedPref = applicationContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("userSelectedMode", "selectTrialVariantExercise")
            editor.apply()
            intent = Intent(this, RecyclerViewActivity::class.java)
            intent.putExtra("userSelectedMode", "selectTrialVariantExercise")
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}