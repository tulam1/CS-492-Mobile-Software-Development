package com.example.android.roomyweather.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomyweather.R
import com.example.android.roomyweather.data.ForecastCity
import com.google.android.material.navigation.NavigationView
import java.util.*

class MainActivity : AppCompatActivity() {
    // Create the upNavigation
    private lateinit var appBarConfig: AppBarConfiguration
    // Create a ViewModel for the Location & Forecast
    private val viewModelLocation: LocationViewModel by viewModels()
    private val viewModelForecast: ForecastViewModel by viewModels()
    // Setup a global adapter
    private val globalCityAdapter = LocationAdapter(::onClickRecentCity)

    // Setup the onCreate function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the global navDrawer
        val drawerLayout: DrawerLayout = findViewById(R.id.main_drawer)

        // Grab the reference to the toolbar
        val toolBar: Toolbar = findViewById(R.id.toolbar_nav)
        setSupportActionBar(toolBar)

        // Setup the recyclerView for the recentCities
        val recentCitiesRV = findViewById<RecyclerView>(R.id.cities_recycler)
        recentCitiesRV.layoutManager = LinearLayoutManager(this)
        recentCitiesRV.setHasFixedSize(true)

        // Setup the adapter for the recycler
        recentCitiesRV.adapter = globalCityAdapter

        // Called the ViewModel to get data
        viewModelLocation.recentCityList.observe(this) { recentCityList ->
            if (recentCityList != null) {
                globalCityAdapter.updateRecentCity(recentCityList)
            }
        }

        // Setup the navBar
        val navHostFragment: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfig = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfig)

        // Setup the NavView
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
    }

    // Create the function that deal with up nav
    override fun onSupportNavigateUp(): Boolean {
        val navControl = findNavController(R.id.nav_host_fragment)
        return navControl.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }

    // Create a click listener to update the data once close from nav
    private fun onClickRecentCity(cityClick: ForecastCity) {
        // Setup a class to be insert in data
        val recentCity = ForecastCity("", 0)

        // Get shared pref of the item
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        val unitsChange = sharedPrefs.getString(getString(R.string.pref_units_key), null)

        // Setup the editor and apply it
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString(getString(R.string.pref_city_key), cityClick.location)
        editor.apply()

        // Get the value and update it to the correct ViewModel
        val cityItem = sharedPrefs.getString(getString(R.string.pref_city_key), null)

        // Setup the data to update the city
        val calendar = Calendar.getInstance()
        val upToDate = calendar.timeInMillis
        recentCity.location = cityItem.toString()
        recentCity.timeStamp = upToDate

        // Insert into the database and update the view
        viewModelLocation.insertSearchCity(recentCity)

        // Close the nav drawer
        val drawerLayout: DrawerLayout = findViewById(R.id.main_drawer)
        drawerLayout.closeDrawers()
    }
}