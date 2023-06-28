package com.vlad.ege_chemistry.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.R
import com.vlad.ege_chemistry.databinding.FragmentStatisticsBinding
import kotlin.math.round

class StatisticsFragment : Fragment() {

    lateinit var binding: FragmentStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )
        checkTestsRes()
        checkTrialVariantsRes()
        binding.deleteResults.setOnClickListener {
            deleteResults()
            checkTestsRes()
            checkTrialVariantsRes()
        }
        return binding.root
    }

    private fun deleteResults() {
        val sharedPref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        for (i in 1..resources.getInteger(R.integer.test_count)){
            editor.remove("test${i}_done")
        }
        editor.remove("pr1_res")
        editor.apply()
    }

    private fun checkTestsRes(){
        val countTests = resources.getInteger(R.integer.test_count)
        var countDoneTests = 0
        var testRes:Boolean
        var string = ""
        val sharedPref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        for (i in 1..countTests){
            testRes = sharedPref.getBoolean("test${i}_done",false)
            if(testRes){
                string += "Тест №$i - Выполнено \n"
                countDoneTests++
            }else{
                string += "Тест №$i - Не выполнено \n"
            }
            binding.testResTextView.text = string
        }
        val percentDoneTests = countDoneTests*100 / countTests
        string = "Тесты: ${percentDoneTests}%"
        binding.testResTitleTextView.text = string
    }
    private fun checkTrialVariantsRes(){
        val sharedPref = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE)
        val pr1Res = sharedPref.getString("pr1_res","-1")?.toInt()
        val string = if(pr1Res == -1){
            "Первый пробник: не приступали"
        }else{
            "Первый пробник: ${pr1Res}/29 правильно \n"
        }
        binding.probnikResTextView.text = string
    }
}