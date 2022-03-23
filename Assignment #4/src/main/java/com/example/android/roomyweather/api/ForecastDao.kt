package com.example.android.roomyweather.api

import androidx.room.*
import com.example.android.roomyweather.data.ForecastCity
import kotlinx.coroutines.flow.Flow

// Create an interface for Dao support
@Dao
interface ForecastDao {
    // Create the insert and deletion of the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationItem: ForecastCity)

    @Delete
    suspend fun delete(locationItem: ForecastCity)

    // Create a query that fetches all the data from Save City into most Recent
    @Query("SELECT * FROM ForecastCity ORDER BY timeStamp DESC")
    fun getRecentCities() : Flow<List<ForecastCity>>
}