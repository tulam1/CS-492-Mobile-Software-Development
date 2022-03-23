package com.example.android.basicweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the RecyclerView for the Weather App & attach the layout manager
        val weatherListRV = findViewById<RecyclerView>(R.id.weather_recycler)
        weatherListRV.layoutManager = LinearLayoutManager(this)
        weatherListRV.setHasFixedSize(true)

        // Tie the Recycler to the adapter
        val adapter = WeatherAdapter()
        weatherListRV.adapter = adapter
    }
}