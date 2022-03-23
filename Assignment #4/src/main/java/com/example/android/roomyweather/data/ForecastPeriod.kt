package com.example.android.roomyweather.data

import com.squareup.moshi.Json
import java.io.Serializable

// Create a data class to store each individual weather data
data class ForecastPeriod(
    @Json(name = "dt_txt") val dateTimeWeather: String,
    @Json(name = "main") val tempData: ForecastTemp,
    @Json(name = "weather") val weatherDesc: List<ShortDescription>,
    @Json(name = "pop") val precipitation: Double
) : Serializable

// Create a data class to access the temperature
data class ForecastTemp(
    @Json(name = "temp_min") val loTemp: Double,
    @Json(name = "temp_max") val hiTemp: Double
) : Serializable

// Create a data class to access the short description
data class ShortDescription(
    @Json(name = "description") val srtDesc: String
) : Serializable

// Create a data class to store the name of the City
data class CityName(
    @Json(name = "name") val cityTownName: String
) : Serializable