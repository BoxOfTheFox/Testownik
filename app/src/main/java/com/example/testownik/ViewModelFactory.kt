package com.example.testownik

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testownik.database.BaseDatabaseDao
//import com.example.testownik.ui.quiz.QuizViewModel
import com.example.testownik.ui.title.TitleViewModel

class ViewModelFactory(private val database: BaseDatabaseDao, private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(QuizViewModel::class.java))
//            return QuizViewModel(database, application) as T
//        else
            if (modelClass.isAssignableFrom(TitleViewModel::class.java))
            return TitleViewModel(database, application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}