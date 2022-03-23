package com.example.android.basicweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

// Create an Adapter to maintain the data associated with the RecyclerView
class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    // Initialize couple of dummy data onto the List
    val oneData = WeatherData("Jan", "21", "30°F", "45°F", "Precipitation: 0%", "Mostly Sunny", "Except for a few afternoon clouds, the day will mainly be sunny. (Winds: NW 10-20 mph)")
    val twoData = WeatherData("Jan", "22", "33°F", "48°F", "Precipitation: 10%", "Sunny", "Sunshine throughout the day with few clouds break. (Winds: WNW 5 mph)")
    val threeData = WeatherData("Jan", "23", "35°F", "47°F", "Precipitation: 2%", "Mainly Cloudy", "Cloudy skies & gloomy through the day. (Winds: N 5-10 mph)")
    val fourData = WeatherData("Jan", "24", "36°F", "50°F", "Precipitation: 14%", "Mainly Sunny", "Sunshine mainly all day. (Winds: NNE 7 mph)")
    val fiveData = WeatherData("Jan", "25", "41°F", "47°F", "Precipitation: 36%", "A.M. Showers", "Morning shower and cloudy for the rest of the day. (Winds: N 9 mph)")
    val sixData = WeatherData("Jan", "26", "38°F", "55°F", "Precipitation: 20%", "A.M. Fog", "Advisory for fog in the early morning and remain cloudy throughout the afternoon. (Winds: NNE 6 mph)")
    val sevenData = WeatherData("Jan", "27", "34°F", "43°F", "Precipitation: 7%", "Few Showers", "Few spot with showers and sun break in between. (Winds: SSE 4 mph)")
    val eightData = WeatherData("Jan", "28", "38°F", "58°F", "Precipitation: 31%", "P.M. Showers", "Clear skies in the morning till afternoon, showers persist in the evening. (Winds: N 5-8 mph)")
    val nineData = WeatherData("Jan", "29", "39°F", "53°F", "Precipitation: 100%", "Heavy Rain", "Heavy rain all day, watch out for flooding & landslide. (Winds: WSW 3-6 mph)")
    val tenData = WeatherData("Jan", "30", "32°F", "40°F", "Precipitation: 50%", "Rain/Snow Showers", "On & off showers throughout the day, chance of flurries mix with rain in the morning. (Winds: ESE 20 mph)")

    // Create a mutable list to store weather dummy data
    val weatherList: MutableList<WeatherData> = mutableListOf(oneData, twoData, threeData,
                                                              fourData, fiveData, sixData,
                                                              sevenData, eightData, nineData, tenData)

    // Create a function to determine how many items that are maintain by the adapter
    override fun getItemCount() = this.weatherList.size

    // Create a function to allocated new ViewHolder when it is needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.weather_view_item, parent, false)

        return ViewHolder(view)
    }

    // Create a function to bound specific data onto the ViewHolder card
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.weatherList[position])
    }

    // Implement the ViewHolder class to handle the element display
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Setup variables to connect with XML to display ViewHolder content
        private val monthData: TextView = view.findViewById(R.id.tv_weather_month)
        private val dayData: TextView = view.findViewById(R.id.tv_weather_day)
        private val lowtempData: TextView = view.findViewById(R.id.tv_low_temp)
        private val hightempData: TextView = view.findViewById(R.id.tv_high_temp)
        private val precipData: TextView = view.findViewById(R.id.tv_precip)
        private val shdescData: TextView = view.findViewById(R.id.tv_short_desc)
        private val cardViewItem = view.findViewById<CardView>(R.id.cv_weather)

        // Setup Init block with OnClickListener using SnackBar
        init {
            view.setOnClickListener() {
                Snackbar.make(cardViewItem,
                              weatherList[absoluteAdapterPosition].longDesc,
                              Snackbar.LENGTH_SHORT).show()
            }
        }

        // Create a binding function to update RecyclerView when it is about to be scrolled
        fun bind(weatherData: WeatherData) {
            this.monthData.text = weatherData.weatherMonth
            this.dayData.text = weatherData.weatherDay
            this.lowtempData.text = weatherData.lowTemp
            this.hightempData.text = weatherData.highTemp
            this.precipData.text = weatherData.precipitation
            this.shdescData.text = weatherData.shortDesc
        }
    }
}