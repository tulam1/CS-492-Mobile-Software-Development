package com.example.android.roomyweather.data

import com.example.android.roomyweather.api.ForecastDao

// Setup the location save via Repository
class LocationRepository(
    private val dao: ForecastDao
) {
    // Setup the function to call action in the background
    suspend fun insertLocation(location: ForecastCity) = dao.insert(location)
    suspend fun deleteLocation(location: ForecastCity) = dao.delete(location)
    fun getRecentCitiesList() = dao.getRecentCities()
}