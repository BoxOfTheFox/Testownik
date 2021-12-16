package com.example.testownik.ui.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.example.testownik.R
import com.example.testownik.databinding.FragmentScoreBinding

class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScoreBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)

        binding
            .endButton
            .setOnClickListener(
                Navigation.createNavigateOnClickListener(
                        ScoreFragmentDirections.actionScoreFragmentToTitleFragment()
                    )
            )

        return binding.root
    }
}