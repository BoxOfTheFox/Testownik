package com.example.testownik.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testownik.database.BaseDatabaseDao
//import com.example.testownik.quiz.QuizViewModel
import com.example.testownik.title.TitleViewModel

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