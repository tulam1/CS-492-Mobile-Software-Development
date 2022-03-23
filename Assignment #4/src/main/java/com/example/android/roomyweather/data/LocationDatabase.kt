package com.example.android.roomyweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.roomyweather.api.ForecastDao

// Setup a const string with database name
const val DATABASE_NAME = "forecast-city-db"

// Create an abstract class that inherit from ROOM
@Database(entities = [ForecastCity::class], version = 1)
abstract class LocationDatabase : RoomDatabase() {
    // Create a getter function of the Dao
    abstract fun forecastDao(): ForecastDao

    // Setup the companion object to build the database
    companion object {
        @Volatile private var instance: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, LocationDatabase::class.java, DATABASE_NAME).build()
    }
}