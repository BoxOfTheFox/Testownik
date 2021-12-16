package com.example.testownik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.testownik.ui.quiz.QuizFragment
import com.example.testownik.ui.FragmentFloatingActionButton
import com.example.testownik.ui.title.TitleFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)

        (supportFragmentManager.findFragmentById(R.id.main_navigation) as NavHostFragment).apply {
            navController.addOnDestinationChangedListener { _, _, _ ->
                fab.hide()
                (childFragmentManager.fragments.first {
                    when (it) {
                        is TitleFragment -> true
                        is QuizFragment -> true
                        else -> TODO()
                    }
                } as FragmentFloatingActionButton).listener(fab)
                fab.show()
            }
        }
    }
}