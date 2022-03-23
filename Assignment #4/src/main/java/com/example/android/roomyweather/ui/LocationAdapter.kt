package com.example.android.roomyweather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.android.roomyweather.R
import com.example.android.roomyweather.data.ForecastCity
import com.example.android.roomyweather.data.ForecastPeriod

class LocationAdapter(private val onClickRecentCity: (ForecastCity) -> Unit)
    : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    // Setup a variable that hold the list of all data in the database
    private var itemCity = mutableListOf<ForecastCity>()

    // Create a function to update list
    fun updateRecentCity(cityItem: List<ForecastCity>) {
        itemCity = cityItem as MutableList<ForecastCity>
        notifyDataSetChanged()
    }

    // Get the amount of item inside the list
    override fun getItemCount() = this.itemCity.size

    // Create the onViewHolder function
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recent_city_item, parent, false)

        return ViewHolder(view, onClickRecentCity)
    }

    // Create the function to recycle the view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(this.itemCity[position])
    }

    // Setup the inner ViewHolder
    inner class ViewHolder(view: View, val onClickRecentCity: (ForecastCity) -> Unit)
        : RecyclerView.ViewHolder(view) {
        // Setup a way to connect the item to the view
        private val cityDisplay: TextView = view.findViewById(R.id.tv_city_name)
        private val itemClick = view.findViewById<CardView>(R.id.cv_cities)

        // Setup a variable to use in the listener
        private var currentCityClick: ForecastCity? = null

        // Set the init to listen for click
        init {
            itemClick.setOnClickListener {
                currentCityClick?.let(onClickRecentCity)
            }
        }

        // Create a binding function
        fun bind(cityItem: ForecastCity) {
            currentCityClick = cityItem
            this.cityDisplay.text = cityItem.location
        }
    }
}