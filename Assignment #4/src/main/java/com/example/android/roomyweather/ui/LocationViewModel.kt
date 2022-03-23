package com.example.android.roomyweather.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.roomyweather.data.ForecastCity
import com.example.android.roomyweather.data.LocationDatabase
import com.example.android.roomyweather.data.LocationRepository
import kotlinx.coroutines.launch

// Setup the ViewModel for the location of cities
class LocationViewModel(application: Application) : AndroidViewModel(application) {
    // Create a var for the repo to connect to the database
    private val repo = LocationRepository(
        LocationDatabase.getInstance(application).forecastDao()
    )

    // Setup a way to connect with Dao to fetch all save cities
    val recentCityList = repo.getRecentCitiesList().asLiveData()

    // Setup the function that will add/delete location
    fun insertSearchCity(searchItem: ForecastCity) {
        viewModelScope.launch {
            repo.insertLocation(searchItem)
        }
    }

    fun deleteSearchCity(searchItem: ForecastCity) {
        viewModelScope.launch {
            repo.deleteLocation(searchItem)
        }
    }
}