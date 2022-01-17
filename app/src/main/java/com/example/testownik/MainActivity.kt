package com.example.testownik

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    interface MainActivityHandler{
        fun onFabClickListener()
        fun deleteButtonClickListener()
    }

    private val currentNavigationFragment: Fragment?
        get() = supportFragmentManager.findFragmentById(R.id.main_navigation)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        bottomAppBar = findViewById(R.id.bottom_app_bar)

        fab.setOnClickListener {
                (currentNavigationFragment as? MainActivityHandler)?.onFabClickListener()
            }

        bottomAppBar.menu.findItem(R.id.delete).apply {
            isVisible = false
        }

        bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.delete -> {
                    (currentNavigationFragment as? MainActivityHandler)?.deleteButtonClickListener()
                    true
                }
                else -> {false}
            }
        }
    }

    fun setMenuItemVisibility(@IdRes id: Int, visible: Boolean){
        bottomAppBar.menu.findItem(id).isVisible = visible
    }
}