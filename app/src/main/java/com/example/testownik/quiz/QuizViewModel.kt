package com.example.testownik.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testownik.database.BaseDatabaseDao

//class QuizViewModel(val database: BaseDatabaseDao, application: Application) :
//    AndroidViewModel(application) {
//    private val _questions: MutableList<Question> = mutableListOf()
//    val questions: MutableList<Question>
//        get() = _questions
//
//    private var _numQuestions = 0
//    val numQuestions: Int
//        get() = _numQuestions
//
//    private val _currentQuestion = MutableLiveData<Question>()
//    val currentQuestion: LiveData<Question>
//        get() = _currentQuestion
//
//    private val _checkBoxAnswers: MutableList<String> = mutableListOf()
//    val checkBoxAnswers: MutableList<String>
//        get() = _checkBoxAnswers
//
//    var timer: Long? = null
//
//    fun addQuestion(question: Question) {
//        _questions.add(question)
//    }
//
//    fun setQuestionsAmount(amount: Int? = null) {
//        _numQuestions = amount ?: _questions.size
//    }
//
//    fun nextQuestion() {
//        _currentQuestion.value = _questions.removeFirstOrNull()
//        _checkBoxAnswers.clear()
//    }
//
//    // randomize the questions and set the first question
//    fun randomizeQuestions() {
//        _questions.shuffle()
//        nextQuestion()
//    }
//
//    private fun isCheckBoxAnswerCorrect(): Boolean {
//        val correctAnswers = _currentQuestion.value?.correctAnswers
//        return correctAnswers?.size == _checkBoxAnswers.size
//                && correctAnswers
//            .asSequence()
//            .mapIndexed { index, element ->
//                element == _checkBoxAnswers[index]
//            }.all { it }
//    }
//
//    fun handleSubmitButton() {
//        if (isCheckBoxAnswerCorrect()) {
//            nextQuestion()
//        } else {
//            // todo show wrong and right answers
//        }
//    }
//}