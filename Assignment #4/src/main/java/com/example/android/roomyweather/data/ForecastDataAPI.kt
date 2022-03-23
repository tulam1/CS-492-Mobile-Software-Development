package com.example.android.roomyweather.data

// Create a data class as the top view of the Forecast JSON file
data class ForecastDataAPI(
    val list: List<ForecastPeriod>,
    val city: CityName
)