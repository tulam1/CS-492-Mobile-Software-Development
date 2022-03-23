package com.example.android.roomyweather.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

// Create a custom data class to store info of save cities
@Entity
data class ForecastCity(
    @PrimaryKey var location: String,
    var timeStamp: Long
) : Serializable