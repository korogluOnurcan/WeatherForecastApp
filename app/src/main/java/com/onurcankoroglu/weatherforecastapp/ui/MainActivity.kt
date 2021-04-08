package com.onurcankoroglu.weatherforecastapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.onurcankoroglu.weatherforecastapp.R
import com.onurcankoroglu.weatherforecastapp.databinding.ActivityMainBinding
import com.onurcankoroglu.weatherforecastapp.util.extensions.gone
import com.onurcankoroglu.weatherforecastapp.util.extensions.visible

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
        setNavMenuVisibility()
    }

    private fun setUpNavigation() {
        val navController = getNavController()
        binding.activityMainBottomNavigationView.setupWithNavController(navController)
    }

    private fun setNavMenuVisibility() {
        val navController = getNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> {
                    binding.activityMainBottomNavigationView.gone()
                }
                R.id.locationInfoFragment -> {
                    binding.activityMainBottomNavigationView.gone()
                }
                else -> {
                    binding.activityMainBottomNavigationView.visible()
                }
            }
        }
    }

    private fun getNavController(): NavController {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.activityMain_navHostFragment) as NavHostFragment
        return navHostFragment.navController
    }

}