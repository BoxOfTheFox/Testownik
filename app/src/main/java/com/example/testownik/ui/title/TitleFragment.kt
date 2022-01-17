package com.example.testownik.ui.title

import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.testownik.MainActivity
import com.example.testownik.R
import com.example.testownik.database.Base
import com.example.testownik.database.BaseDatabase
import com.example.testownik.databinding.FragmentTitleBinding
import com.example.testownik.ViewModelFactory
import com.example.testownik.database.BaseWithQuestions
import com.google.android.material.snackbar.Snackbar

class TitleFragment : Fragment(), MainActivity.MainActivityHandler,
    BaseSelectionAdapter.BaseSelectionAdapterListener {

    private lateinit var viewModel: TitleViewModel
    private lateinit var binding: FragmentTitleBinding
    private lateinit var selectionTracker: SelectionTracker<Long>

    // fixme dodanie błędnej bazy
    // fixme ArchiveSwipeActionDrawable kolorki kosza
    private val chooseQuestionFolder =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri ->
            uri?.let {
                viewModel.insertBase(
                    requireContext(),
                    it,
                    Base(title = it.lastPathSegment!!.split("/").last(), state = BaseState.ACTIVE)
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

        selectionTracker = SelectionTracker.Builder(
            "card_selection",
            binding.quizRecyclerView,
            BaseSelectionAdapter.KeyProvider(binding.quizRecyclerView),
            BaseSelectionAdapter.DetailsLookup(binding.quizRecyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()

        adapter.selectionTracker = selectionTracker

        viewModel.bases.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onSelectionChanged() {
                if (selectionTracker.selection.size() > 0)
                    (activity as MainActivity).setMenuItemVisibility(R.id.delete,true)
                else
                    (activity as MainActivity).setMenuItemVisibility(R.id.delete,false)
            }
        })

        return binding.root
    }

    override fun onBaseArchived(baseWithQuestions: BaseWithQuestions?) {
        if (null == baseWithQuestions)
            return
        
        Snackbar.make(
            requireActivity().findViewById(R.id.mainActivity),
            "Usunięto",
            Snackbar.LENGTH_SHORT
        )
            .setAction("Undo") {}
            .setAnchorView(requireActivity().findViewById(R.id.fab))
//                potencjalnie nie działa poniżej API 23
//                todo przerobić na rozszerzenie
            .addCallback(object: Snackbar.Callback(){
                override fun onShown(sb: Snackbar?) {
                    selectionTracker.clearSelection()
                    viewModel.setBaseDeleted(baseWithQuestions.base.id)
                    selectionTracker.clearSelection()
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (DISMISS_EVENT_TIMEOUT == event || DISMISS_EVENT_CONSECUTIVE == event) {
                        viewModel.clearBasesWithDeleteState()
                    }else{
                        viewModel.setBasesDeletedToActive()
                    }
                    selectionTracker.clearSelection()
                }
            })
            .show()
    }

    override fun onFabClickListener() {
        chooseQuestionFolder.launch(null)
    }

    override fun deleteButtonClickListener() {
        val ids = selectionTracker.selection.map { it }

        Snackbar.make(
            requireActivity().findViewById(R.id.mainActivity),
            "Usunięto",
            Snackbar.LENGTH_SHORT
        )
            .setAction("Undo") {}
            .setAnchorView(requireActivity().findViewById(R.id.fab))
//                potencjalnie nie działa poniżej API 23
//                todo przerobić na rozszerzenie
            .addCallback(object: Snackbar.Callback(){
                override fun onShown(sb: Snackbar?) {
                    selectionTracker.clearSelection()
                    viewModel.setBasesDeleted(ids)
                    selectionTracker.clearSelection()
                }

                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (DISMISS_EVENT_TIMEOUT == event || DISMISS_EVENT_CONSECUTIVE == event) {
                        viewModel.clearBasesWithDeleteState()
                    }else{
                        viewModel.setBasesDeletedToActive()
                    }
                    selectionTracker.clearSelection()
                }
            })
            .show()
    }
}

