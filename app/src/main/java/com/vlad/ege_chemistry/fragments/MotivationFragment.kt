package com.vlad.ege_chemistry.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.vlad.ege_chemistry.R
import com.vlad.ege_chemistry.databinding.FragmentMotivationBinding

class MotivationFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMotivationBinding>(
            inflater, R.layout.fragment_motivation, container, false
        )
        return binding.root
    }
}