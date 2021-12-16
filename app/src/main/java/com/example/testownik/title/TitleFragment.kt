package com.example.testownik.title

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.testownik.R
import com.example.testownik.database.Base
import com.example.testownik.database.BaseDatabase
import com.example.testownik.databinding.FragmentTitleBinding
import com.example.testownik.viewModelFactory.ViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

// fixme nawigacja - po powrocie z zakończonej gry back powraca do ScoreFragment
class TitleFragment : Fragment() {

    private lateinit var viewModel: TitleViewModel

    private val chooseQuestionFolder =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                viewModel.insertBase(
                    requireContext(),
                    it,
                    Base(title = it.lastPathSegment!!.split("/").last())
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = BaseDatabase.getInstance(requireContext()).BaseDatabaseDao

        val viewModelFactory = ViewModelFactory(database, requireActivity().application)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TitleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentTitleBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        val adapter = BaseSelectionAdapter()
        binding.quizList.adapter = adapter

        viewModel.bases.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

//        requireActivity().findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
//            chooseQuestionFolder.launch(null)
//        }

        return binding.root
    }
}