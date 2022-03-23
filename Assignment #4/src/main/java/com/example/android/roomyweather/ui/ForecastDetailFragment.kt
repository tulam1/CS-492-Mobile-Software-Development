package com.example.android.roomyweather.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.android.roomyweather.data.ForecastPeriod
import com.example.android.roomyweather.R
import com.example.android.roomyweather.data.CityName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

class ForecastDetailFragment : Fragment(R.layout.activity_forecast_detail) {
    // Create an args to accepts the data coming in
    private val args: ForecastDetailFragmentArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind to the city name
        view.findViewById<TextView>(R.id.tv_city).text = args.city.cityTownName

        // Get the specific layout for the time & date
        val dateTimeParse = args.detail.dateTimeWeather
        val dateTimePattern = "yyyy-MM-dd H:mm:ss"
        val dateTimeValue: LocalDateTime = LocalDateTime.parse(dateTimeParse, DateTimeFormatter.ofPattern(dateTimePattern))
        val weatherDate: LocalDate = dateTimeValue.toLocalDate()
        val weatherTime: LocalTime = dateTimeValue.toLocalTime()

        // Get the month from the Date parsing data
        val monthShort: Month = Month.from(weatherDate)

        // Create a format for 12-hours Time
        val timePattern: String = "H:mm a"
        val formatTime = weatherTime.format(DateTimeFormatter.ofPattern(timePattern))

        // Setup a way to convert units based on preferences
        var tempUnits = "°K"
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val getUnits = sharedPrefs.getString(getString(R.string.pref_units_key), null)
        if (getUnits == getString(R.string.units_value_imperial)) {
            tempUnits = "°F"
        }

        if (getUnits == getString(R.string.units_value_metric)) {
            tempUnits = "°C"
        }

        if (getUnits == getString(R.string.units_value_standard)) {
            tempUnits = "°K"
        }

        // Take the data from Intent and plug into UI on XML
        view.findViewById<TextView>(R.id.tv_detail_precip).text = "Precipitation: " + (args.detail.precipitation * 100.0).toInt().toString() + "%"
        view.findViewById<TextView>(R.id.tv_detail_low).text = "Low: " + args.detail.tempData.loTemp.toInt().toString() + tempUnits
        view.findViewById<TextView>(R.id.tv_detail_high).text = "High: " + args.detail.tempData.hiTemp.toInt().toString() + tempUnits
        view.findViewById<TextView>(R.id.tv_detail_desc).text = args.detail.weatherDesc[0].srtDesc
        view.findViewById<TextView>(R.id.tv_detail_date).text = monthShort.getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + weatherDate.dayOfMonth.toString() + ","
        view.findViewById<TextView>(R.id.tv_detail_time).text = formatTime.toString()
    }
}