package com.vlad.ege_chemistry.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.R
import com.vlad.ege_chemistry.RecyclerViewActivity
import com.vlad.ege_chemistry.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater, R.layout.fragment_main, container, false
        )

        binding.buttonTeory.setOnClickListener {
            val intent = Intent(requireContext(), RecyclerViewActivity::class.java)
            intent.putExtra("type","teory")
            startActivity(intent)
        }
        binding.buttonTest.setOnClickListener {
            val intent = Intent(requireContext(), RecyclerViewActivity::class.java)
            intent.putExtra("type","test")
            startActivity(intent)
        }
        binding.buttonTrialVariants.setOnClickListener {
            val intent = Intent(requireContext(), RecyclerViewActivity::class.java)
            intent.putExtra("type","trialVariants")
            startActivity(intent)
        }

        return binding.root
    }

}