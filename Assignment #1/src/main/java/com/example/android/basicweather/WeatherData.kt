package com.example.android.basicweather

// Create a data class to store each individual weather data
data class WeatherData(
    val weatherMonth: String,
    val weatherDay: String,
    var lowTemp: String,
    var highTemp: String,
    var precipitation: String,
    var shortDesc: String,
    var longDesc: String)
