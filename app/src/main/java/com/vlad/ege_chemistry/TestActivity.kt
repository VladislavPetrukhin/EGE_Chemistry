package com.vlad.ege_chemistry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    var position = 0
    var numberOfQuestion:Int = 1
    lateinit var binding:ActivityTestBinding
    var userAnswers = ArrayList<String>()
    var finishedTest = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityTestBinding>(this, R.layout.activity_test)
        position = intent.getIntExtra("position",1)

        inflateExercise()

        binding.testAnswerButton.setOnClickListener {
            numberOfQuestion++
            userAnswers.add(binding.testEditText.text.toString().trim())
            if (finishedTest) {
//                var intent = Intent(this,RecyclerViewActivity::class.java)
//                intent.putExtra("type","test")
//                startActivity(intent)
                finish()
            } else if (numberOfQuestion > resources.getInteger(R.integer.exercisesInTests_count)) {
                    finishTest()
                } else {
                    inflateExercise()
                }
        }
    }

    private fun inflateExercise() {
        var textResourceId = resources.getIdentifier("test${position}_$numberOfQuestion","string",packageName)
        binding.testTextView.text = resources.getString(textResourceId)
        binding.testEditText.setText("")
    }

    private fun finishTest() {
        var textResourceId = resources.getIdentifier("testAnswer$position","string",packageName)
        var correctAnswers = resources.getString(textResourceId).split(":")
        var quantityOfCorrectAnswers = 0
        for (i in correctAnswers.indices){
            if(userAnswers[i] == correctAnswers[i]){
                quantityOfCorrectAnswers++
            }
        }
        if (quantityOfCorrectAnswers == correctAnswers.size){
            binding.testTextView.text = resources.getString(R.string.correct_answers)
        }else{
            var string =
                resources.getString(R.string.not_correct_answers) + quantityOfCorrectAnswers +
                        "/" + resources.getInteger(R.integer.exercisesInTests_count)
            binding.testTextView.text = string
        }

        binding.testEditText.visibility = View.GONE
        binding.testAnswerButton.text = "Окей"
        finishedTest = true
    }

}