package com.example.android.roomyweather.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomyweather.R
import com.example.android.roomyweather.data.CityName
import com.example.android.roomyweather.data.ForecastDataAPI
import com.example.android.roomyweather.data.ForecastPeriod
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.Month
import java.time.format.TextStyle
import java.util.*

// Create an Adapter to maintain the data associated with the RecyclerView
class ForecastAdapter(private val onWeatherViewClick: (ForecastPeriod) -> Unit)
    : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    // Create a list of data Forecast & city name
    var dataForecast = listOf<ForecastPeriod>()
    var cityNaming: CityName? = null

    // Setup a function to update data into the Forecast class
    fun updateWeatherForecast(newForecastData: ForecastDataAPI?) {
        dataForecast = newForecastData?.list ?: listOf()
        cityNaming = newForecastData?.city
        notifyDataSetChanged()
    }

    // Create a function to determine how many items that are maintain by the adapter
    override fun getItemCount() = this.dataForecast.size

    // Create a function to allocated new ViewHolder when it is needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_list_item, parent, false)

        return ViewHolder(view, onWeatherViewClick)
    }

    // Create a function to bound specific data onto the ViewHolder card
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.dataForecast[position])
    }

    // Implement the ViewHolder class to handle the element display
    inner class ViewHolder(itemView: View, val onWeatherClick: (ForecastPeriod) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        // Setup variables to connect with XML to display ViewHolder content
        private val monthData: TextView = itemView.findViewById(R.id.tv_weather_month)
        private val dayData: TextView = itemView.findViewById(R.id.tv_weather_day)
        private val timeData: TextView = itemView.findViewById(R.id.tv_time)
        private val lowTempData: TextView = itemView.findViewById(R.id.tv_low_temp)
        private val highTempData: TextView = itemView.findViewById(R.id.tv_high_temp)
        private val precipData: TextView = itemView.findViewById(R.id.tv_precip)
        private val srtdescData: TextView = itemView.findViewById(R.id.tv_short_desc)
        private val cardViewItem = itemView.findViewById<CardView>(R.id.cv_weather)

        // Setup a variable that will handle the onClick event
        private var currentWeatherList: ForecastPeriod? = null

        // Setup the init for onClick event for new Activity
        init {
            cardViewItem.setOnClickListener {
                currentWeatherList?.let(onWeatherClick)
            }
        }

        // Create a binding function to update RecyclerView when it is about to be scrolled
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(weatherData: ForecastPeriod) {
            // Assign the data to a new List for onClick to work & setup the context to get preferences
            currentWeatherList = weatherData
            val contextItem = itemView.context

            // Create bunch of variables to parse Date & Time separately from string
            val dateTimeParse = weatherData.dateTimeWeather
            val dateTimePattern = "yyyy-MM-dd H:mm:ss"
            val dateTimeValue: LocalDateTime = LocalDateTime.parse(dateTimeParse, DateTimeFormatter.ofPattern(dateTimePattern))
            val weatherDate: LocalDate = dateTimeValue.toLocalDate()
            val weatherTime: LocalTime = dateTimeValue.toLocalTime()

            // Get the month from the Date parsing data
            val monthShort: Month = Month.from(weatherDate)

            // Create a format for 12-hours Time
            val timePattern: String = "H:mm a"
            val formatTime = weatherTime.format(DateTimeFormatter.ofPattern(timePattern))

            // Create a way to filter out the units based on Preferences
            var tempUnits = "°K"
            val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(contextItem)
            val unitChange = sharedPrefs.getString(contextItem.getString(R.string.pref_units_key), null)
            if (unitChange == contextItem.getString(R.string.units_value_imperial)) {
                tempUnits = "°F"
            }

            if (unitChange == contextItem.getString(R.string.units_value_metric)) {
                tempUnits = "°C"
            }

            if (unitChange == contextItem.getString(R.string.units_value_standard)) {
                tempUnits = "°K"
            }

            // Bind all data onto the XML TextView
            this.monthData.text = monthShort.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            this.dayData.text = weatherDate.dayOfMonth.toString()
            this.timeData.text = formatTime.toString()
            this.lowTempData.text = weatherData.tempData.loTemp.toInt().toString() + tempUnits
            this.highTempData.text = weatherData.tempData.hiTemp.toInt().toString() + tempUnits
            this.precipData.text = "Precipitation: " + (weatherData.precipitation * 100.0).toInt().toString() + "%"
            this.srtdescData.text = weatherData.weatherDesc[0].srtDesc
        }
    }
}