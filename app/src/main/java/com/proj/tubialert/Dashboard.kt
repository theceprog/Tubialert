package com.proj.tubialert

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.proj.tubialert.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener { // Implement listener

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var openDrawerIcon: ImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        openDrawerIcon = binding.appBarDashboard.imageViewOpenDrawerIcon
        val navView: NavigationView = binding.navView
        navController =
            findNavController(R.id.nav_host_fragment_content_dashboard) // Assuming this is in content_dashboard.xml

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.navhelp,
                R.id.navabout,
                R.id.navnotif,
                R.id.navprofile,
                R.id.navterms
            ), drawerLayout
        )

        openDrawerIcon.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        navView.setNavigationItemSelectedListener(this)
    }

    // This method is called when an item in the NavigationView (sidebar) is selected.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Example: Navigate to home or show a toast
                navController.navigate(R.id.nav_home) // Make sure this ID is a valid destination in your nav graph
            }

            R.id.navnotif -> {
                navController.navigate(R.id.navnotif)
            }

            R.id.navprofile -> {
                navController.navigate(R.id.navprofile)
            }

            R.id.navhelp -> {
                navController.navigate(R.id.navhelp)
            }

            R.id.navterms -> {
                navController.navigate(R.id.navterms)
            }

            R.id.navabout -> {
                navController.navigate(R.id.navabout)
            }

            else -> return false
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
