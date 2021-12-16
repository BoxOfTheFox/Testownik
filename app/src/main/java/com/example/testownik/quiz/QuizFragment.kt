package com.example.testownik.quiz

import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.testownik.R
import com.example.testownik.database.BaseDatabase
import com.example.testownik.databinding.FragmentQuizBinding
import com.example.testownik.viewModelFactory.ViewModelFactory
import java.nio.charset.Charset

//class QuizFragment : Fragment() {
//
//    private lateinit var viewModel: QuizViewModel
//
//    private lateinit var binding: FragmentQuizBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        val database = BaseDatabase.getInstance(requireContext()).BaseDatabaseDao
//
//        val viewModelFactory = ViewModelFactory(database, requireActivity().application)
//
//        viewModel = ViewModelProvider(this, viewModelFactory).get(QuizViewModel::class.java)
//
//        // todo find way to move it to ViewModel
//        val uri = Uri.parse(QuizFragmentArgs.fromBundle(requireArguments()).uriString)
//        DocumentFile.fromTreeUri(requireContext(), uri)
//            ?.listFiles()
//            ?.forEach {
//                requireContext()
//                    .contentResolver
//                    .openInputStream(it.uri)
//                    ?.readBytes()
//                    ?.toString(Charset.defaultCharset())
//                    ?.lines()
//                    ?.let { lst ->
//                        viewModel.addQuestion(
//                            Question(
//                                text = lst[1],
//                                answers = lst.subList(2, lst.size-1),
//                                correctAnswers = lst[0].subSequence(1, lst[0].length)
//                                    .mapIndexed { index, c -> if (c == '1') lst.subList(2, lst.size-1)[index] else "" }
//                                    .filter { it != "" }
//                            )
//                        )
//                    }
//            }
//        viewModel.setQuestionsAmount()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)
//
//        viewModel.randomizeQuestions()
//
//        binding.game = this
//        binding.viewModel = viewModel
//        binding.lifecycleOwner = this
//
//        // todo move logic to ViewModel
//        val adapter = QuestionAdapter { answer, checkbox ->
//            if (checkbox.isChecked)
//                viewModel.checkBoxAnswers.add(answer)
//            else
//                viewModel.checkBoxAnswers.remove(answer)
//        }
//        binding.answersList.adapter = adapter
//
//        viewModel.currentQuestion.observe(viewLifecycleOwner, { question ->
//            question?.let {
//                adapter.submitList(it.answers.shuffled())
//                setSupportActionBar()
//            }?: findNavController(this)
//                    .navigate(
//                        QuizFragmentDirections.actionQuizFragmentToScoreFragment())
//
//        })
//
//        return binding.root
//    }
//
//    private fun setSupportActionBar() {
//        (activity as AppCompatActivity).supportActionBar?.title =
//            getString(R.string.title_android_trivia_question,
//                viewModel.numQuestions - viewModel.questions.size, viewModel.numQuestions)
//    }
//
//    // todo clean up
//    override fun onResume() {
//        super.onResume()
//        binding.timer.base = viewModel.timer?.let {
//            binding.timer.base + SystemClock.elapsedRealtime() - it
//        }?: SystemClock.elapsedRealtime()
//        binding.timer.start()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        viewModel.timer = SystemClock.elapsedRealtime()
//        binding.timer.stop()
//
//    }
//}