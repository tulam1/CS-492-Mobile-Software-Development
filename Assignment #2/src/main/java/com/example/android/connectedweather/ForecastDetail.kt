package com.example.android.connectedweather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.android.connectedweather.data.ForecastPeriod
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

// Create a constant string for Intent
const val EXTRA_FORECAST = "ForecastPeriod"

class ForecastDetail : AppCompatActivity() {
    // Create a variable for the Weather Data
    private var weatherForecast: ForecastPeriod? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_detail)

        // Call intent to load the data and check data first to make sure if does have data
        if (intent != null && intent.hasExtra(EXTRA_FORECAST)) {
            weatherForecast = intent.getSerializableExtra(EXTRA_FORECAST) as ForecastPeriod

            // Get the specific layout for the time & date
            val dateTimeParse = weatherForecast!!.dateTimeWeather
            val dateTimePattern = "yyyy-MM-dd H:mm:ss"
            val dateTimeValue: LocalDateTime = LocalDateTime.parse(dateTimeParse, DateTimeFormatter.ofPattern(dateTimePattern))
            val weatherDate: LocalDate = dateTimeValue.toLocalDate()
            val weatherTime: LocalTime = dateTimeValue.toLocalTime()

            // Get the month from the Date parsing data
            val monthShort: Month = Month.from(weatherDate)

            // Create a format for 12-hours Time
            val timePattern: String = "H:mm a"
            val formatTime = weatherTime.format(DateTimeFormatter.ofPattern(timePattern))

            // Take the data from Intent and plug into UI on XML
            findViewById<TextView>(R.id.tv_detail_precip).text = "Precipitation: " + (weatherForecast!!.precipitation * 100.0).toInt().toString() + "%"
            findViewById<TextView>(R.id.tv_detail_low).text = "Low: " + weatherForecast!!.tempData.loTemp.toInt().toString() + "°F"
            findViewById<TextView>(R.id.tv_detail_high).text = "High: " + weatherForecast!!.tempData.hiTemp.toInt().toString() + "°F"
            findViewById<TextView>(R.id.tv_detail_desc).text = weatherForecast!!.weatherDesc[0].srtDesc
            findViewById<TextView>(R.id.tv_detail_date).text = monthShort.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + weatherDate.dayOfMonth.toString() + ","
            findViewById<TextView>(R.id.tv_detail_time).text = formatTime.toString()
        }
    }
}