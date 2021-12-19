package com.example.testownik

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.testownik.ui.FragmentFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.main_navigation)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.activity_main)

        findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                (currentNavigationFragment as? FragmentFloatingActionButton)?.listener()
            }
        }
    }
}