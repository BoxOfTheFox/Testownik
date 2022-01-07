package com.example.testownik.ui.title

import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.testownik.R
import com.example.testownik.database.Base
import com.example.testownik.database.BaseDatabase
import com.example.testownik.databinding.FragmentTitleBinding
import com.example.testownik.ui.FragmentFloatingActionButton
import com.example.testownik.ViewModelFactory
import com.example.testownik.database.BaseWithQuestions
import com.google.android.material.snackbar.Snackbar

class TitleFragment : Fragment(), FragmentFloatingActionButton,
    BaseSelectionAdapter.BaseSelectionAdapterListener {

    private lateinit var viewModel: TitleViewModel
    private lateinit var binding: FragmentTitleBinding

    // fixme dodanie błędnej bazy
    // fixme ArchiveSwipeActionDrawable kolorki kosza
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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_title,
            container,
            false
        )

        val adapter = BaseSelectionAdapter(this)

        val helper = ItemTouchHelper(ArchiveSwipeActionCallback())

        binding.quizRecyclerView.apply {
            this.adapter = adapter
            helper.attachToRecyclerView(this)
        }

        viewModel.bases.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun listener() {
        chooseQuestionFolder.launch(null)
    }

//    fixme questions aren't removed on time
    override fun onBaseArchived(baseWithQuestions: BaseWithQuestions?) {
        viewModel.removeBase(baseWithQuestions?.base)
        Snackbar.make(
            requireActivity().findViewById(R.id.mainActivity),
            "Usunięto",
            Snackbar.LENGTH_SHORT
        ).setAction("Undo") {
            viewModel.insertBase(baseWithQuestions?.base)
            baseWithQuestions?.questions?.forEach {
                viewModel.insertQuestion(it)
            }
        }
            .setAnchorView(requireActivity().findViewById(R.id.fab))
            .show()
    }
}

