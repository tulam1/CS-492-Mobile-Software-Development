package com.example.android.connectedweather

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.android.connectedweather.data.ForecastPeriod
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MainActivity : AppCompatActivity() {
    // Create a late-init using Volley Request Queue
    private lateinit var requestQueue: RequestQueue
    // Create a based API string of weather & concatenate with hard-coded in weather's city
    private val weatherBaseAPI: String = "https://api.openweathermap.org/data/2.5/forecast"
    private val weatherAPI: String = "$weatherBaseAPI?q=Corvallis,OR,US&units=imperial&appid=affcfb1469412870165a706ade841fd7"
    // Create a val for ForecastAdapter for global usage
    private val weatherForecastAdapter = ForecastAdapter(::onWeatherViewClick)
    // Create variable to access the Loading of API & error handler
    private lateinit var weatherListRV: RecyclerView
    private lateinit var errorLoading: TextView
    private lateinit var loadIndicator: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the request queue using Volley to initialize HTTP
        requestQueue = Volley.newRequestQueue(this)

        // Setup the loading indicator & error to match with XML
        loadIndicator = findViewById(R.id.loading_api)
        errorLoading = findViewById(R.id.error_msg)

        // Setup the RecyclerView for the Weather App & attach the layout manager
        weatherListRV = findViewById(R.id.weather_recycler)
        weatherListRV.layoutManager = LinearLayoutManager(this)
        weatherListRV.setHasFixedSize(true)

        // Tie the Recycler to the adapter
        weatherListRV.adapter = weatherForecastAdapter

        // Call the API search to fetch the data from API
        weatherAPISearch()
    }

    // Create an OnCreate option(s) menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    // Create a clickable map options
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_maps -> {
                viewWeatherMap()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Create a function to view map of weather data
    private fun viewWeatherMap() {
        val uriIntent = Uri.parse("geo:0,0?q=Corvallis")
        val mapIntent = Intent(Intent.ACTION_VIEW, uriIntent)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    // Create a function to setup the GET request for HTTP
    private fun weatherAPISearch() {
        // Setup the Moshi to help parse JSON data from API
        val moshiHelper = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        // Setup the adapter for JSON
        val jsonAdapter: JsonAdapter<WeatherAPIResults> = moshiHelper.adapter(WeatherAPIResults::class.java)

        // Setup the GET to handle GET request, success, and error
        val req = StringRequest(
            Request.Method.GET,
            weatherAPI,
            {
                var results = jsonAdapter.fromJson(it)
                Log.d("MainActivity", results.toString())
                weatherForecastAdapter.updateWeatherForecast(results?.list)
                loadIndicator.visibility = View.INVISIBLE
                weatherListRV.visibility = View.VISIBLE
            },
            {
                Log.d("MainActivity", "Error! fetching from $weatherAPI: ${it.message}")
                loadIndicator.visibility = View.INVISIBLE
                errorLoading.visibility = View.VISIBLE
            }
        )

        // Show the loading indicator & hide other
        loadIndicator.visibility = View.VISIBLE
        weatherListRV.visibility = View.INVISIBLE
        errorLoading.visibility = View.INVISIBLE
        // Add the request to queue once it is process with Volley
        requestQueue.add(req)
    }

    // Setup a class to handle a click on ViewHolder to open a new Activity
    private fun onWeatherViewClick(weatherClick: ForecastPeriod) {
        // Setup the Intent block to connect to new Activity [Explicit Intent]
        val intent = Intent(this, ForecastDetail::class.java)

        // Setup a package to pass info onto the Intent
        intent.putExtra(EXTRA_FORECAST, weatherClick)
        startActivity(intent)
    }

    // Setup a class handling the top overview of the API list of weather
    private data class WeatherAPIResults(
        val list: List<ForecastPeriod>
    )
}