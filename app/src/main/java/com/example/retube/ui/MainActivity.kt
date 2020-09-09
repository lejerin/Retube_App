package com.example.retube.ui;

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.retube.R

import com.example.retube.fragment.StatFragment
import com.example.retube.ui.home.HomeFragment
import com.example.retube.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val homeFragment: HomeFragment = HomeFragment()
    private val statFragment: StatFragment = StatFragment()
    private val searchFragment: SearchFragment = SearchFragment()

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
                        setFragment(searchFragment)
                        //    getSupportActionBar().setTitle("Search");
                        true
                    }
                    R.id.menu_Playlist -> {
                        setFragment(statFragment)
                        //   getSupportActionBar().setTitle("Playlist");
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