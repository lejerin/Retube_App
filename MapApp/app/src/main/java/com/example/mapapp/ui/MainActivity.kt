package com.example.mapapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mapapp.R
import com.example.mapapp.ui.home.HomeFragment
import com.example.mapapp.ui.like.LikeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val homeFragment: HomeFragment =
        HomeFragment()
    private val pageFragment: LikeFragment =
        LikeFragment()

    private lateinit var menuBawah: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        menuBawah = findViewById(R.id.menu_bawah)
        setFragment(homeFragment)


        menuBawah.setSelectedItemId(R.id.menu_home)
        menuBawah.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            if (menuItem.isChecked) {
                true
            } else {
                when (menuItem.itemId) {
                    R.id.menu_search -> {
                        setFragment(pageFragment)
                        //    getSupportActionBar().setTitle("Search");
                        true
                    }
                    else -> {
                        setFragment(homeFragment)
                        //   getSupportActionBar().setTitle("Home");
                        true
                    }
                }
            }
        })
    }

    private fun setFragment(fragment: Fragment) {
        val ft =
            supportFragmentManager.beginTransaction()
        ft.replace(R.id.main_frame, fragment)
        ft.commit()
    }
}