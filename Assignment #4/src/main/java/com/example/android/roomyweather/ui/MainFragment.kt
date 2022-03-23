package com.example.android.roomyweather.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomyweather.R
import com.example.android.roomyweather.data.ForecastCity
import com.example.android.roomyweather.data.ForecastLoadingStatus
import com.example.android.roomyweather.data.ForecastPeriod
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainFragment : Fragment(R.layout.activity_main_fragment) {
    // Create a val for ForecastAdapter for global usage
    private val weatherForecastAdapter = ForecastAdapter(::onWeatherViewClick)
    // Add in ViewModel for forecast & city
    private val viewModelDisplay: ForecastViewModel by viewModels()
    private val viewModelLocation: LocationViewModel by viewModels()
    // Create an empty database
    private var recentCity = ForecastCity("", 0)
    // Create a variable to store in the unit and city
    private var onCreateUnit: String? = null
    private var onCreateCity: String? = null
    // Create variable to access the Loading of API & error handler
    private lateinit var weatherListRV: RecyclerView
    private lateinit var errorLoading: TextView
    private lateinit var loadIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Things are in the app bar
        setHasOptionsMenu(true)

        // Setup the loading indicator & error to match with XML
        loadIndicator = view.findViewById(R.id.loading_api)
        errorLoading = view.findViewById(R.id.error_msg)

        // Setup the RecyclerView for the Weather App & attach the layout manager
        weatherListRV = view.findViewById(R.id.weather_recycler)
        weatherListRV.layoutManager = LinearLayoutManager(requireContext())
        weatherListRV.setHasFixedSize(true)

        // Tie the Recycler to the adapter
        weatherListRV.adapter = weatherForecastAdapter

        // Grab the LiveData Object in MainActivity
        viewModelDisplay.weatherResults.observe(viewLifecycleOwner) { weatherResults ->
            weatherForecastAdapter.updateWeatherForecast(weatherResults)
        }

        // Setup the Loading Status for the View
        viewModelDisplay.loadingStatus.observe(viewLifecycleOwner) { loadingStatus ->
            when (loadingStatus) {
                ForecastLoadingStatus.LOADING -> {
                    // Show the loading indicator & hide other
                    loadIndicator.visibility = View.VISIBLE
                    weatherListRV.visibility = View.INVISIBLE
                    errorLoading.visibility = View.INVISIBLE
                }

                ForecastLoadingStatus.ERROR -> {
                    // Show error
                    loadIndicator.visibility = View.INVISIBLE
                    errorLoading.visibility = View.VISIBLE
                    weatherListRV.visibility = View.INVISIBLE
                }

                else -> {
                    // Show success loading
                    loadIndicator.visibility = View.INVISIBLE
                    weatherListRV.visibility = View.VISIBLE
                    errorLoading.visibility = View.INVISIBLE
                }
            }
        }

        // Create a shared preferences to get the default value
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val unitsMeasurement = sharedPrefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_default))
        val citySearch = sharedPrefs.getString(getString(R.string.pref_city_key), getString(R.string.pref_city_default))

        // Copy the value onto global variable
        onCreateCity = citySearch
        onCreateUnit = unitsMeasurement

        // Call the model to be use for database
        val calendar = Calendar.getInstance()
        val upToDate = calendar.timeInMillis
        recentCity.location = onCreateCity.toString()
        recentCity.timeStamp = upToDate
        viewModelLocation.insertSearchCity(recentCity)
        
        // Call the API via the ViewModel
        viewModelDisplay.loadWeatherResults(citySearch, unitsMeasurement)
    }

    // Create a listener for the click of the SharedPreferences
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, str ->
        // Called the function to update the data
        updateForecastData()
    }

    // Create a function to get the information to update on screen
    private fun updateForecastData() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val unitUpdate = sharedPrefs.getString(getString(R.string.pref_units_key), null)
        val cityUpdate = sharedPrefs.getString(getString(R.string.pref_city_key), null)
        viewModelDisplay.loadWeatherResults(cityUpdate, unitUpdate)
    }

    // Implement section for onResume & onPause
    override fun onResume() {
        Log.d("MainFragment", "onResume()")
        val checkPreference = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val unitChange = checkPreference.getString(getString(R.string.pref_units_key), null)
        val cityChange = checkPreference.getString(getString(R.string.pref_city_key), null)
        // Check to see if the value are the same, then just resume the session
        if (onCreateCity == cityChange && onCreateUnit == unitChange) {
            super.onResume()
        }

        // If one item change, update the API to load the new data
        else {
            // Add into database if both cities are different
            if (cityChange != onCreateCity) {
                val calendar = Calendar.getInstance()
                val upToDate = calendar.timeInMillis
                recentCity.location = cityChange.toString()
                recentCity.timeStamp = upToDate

                // Call the viewModel to add it in
                viewModelLocation.insertSearchCity(recentCity)
            }

            onCreateCity = cityChange
            onCreateUnit = unitChange
            viewModelDisplay.loadWeatherResults(cityChange, unitChange)
            super.onResume()
        }

        // Register the onClick event for the SharedPreferences
        checkPreference.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        Log.d("MainFragment", "onPause()")
        super.onPause()
    }

    // Create an OnCreate option(s) menu
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
    }

    // Create a clickable map & setting options on the Main Activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_maps -> {
                viewWeatherMap()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Create a function to view map of where weather data is coming from
    private fun viewWeatherMap() {
        val uriIntent = Uri.parse("geo:0,0?q=" + weatherForecastAdapter.cityNaming?.cityTownName)
        val mapIntent = Intent(Intent.ACTION_VIEW, uriIntent)
        mapIntent.setPackage("com.google.android.apps.maps")
        // Set up a try and catch block in case of fail implicit Intent
        try {
            startActivity(mapIntent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(
                requireView(),
                R.string.map_error,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    // Setup a class to handle a click on ViewHolder to open a new Activity
    private fun onWeatherViewClick(weatherClick: ForecastPeriod) {
        val direction = weatherForecastAdapter.cityNaming?.let {
            MainFragmentDirections.mainToDetail(weatherClick,
                it
            )
        }

        if (direction != null) {
            findNavController().navigate(direction)
        }
    }
}