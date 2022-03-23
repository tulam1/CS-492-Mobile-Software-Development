package com.example.android.roomyweather.data

import com.example.android.roomyweather.api.ForecastService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

// Create a repository for API call for weather
class ForecastRepository(
    // Setup some special val to help out the repository
    private val service: ForecastService,
    private val ioDispatch: CoroutineDispatcher = Dispatchers.IO
) {
    // Create a load repo for weather
    suspend fun loadRepositoryWeather(query: String?, units: String?): Result<ForecastDataAPI> =
        withContext(ioDispatch) {
            try {
                // Make a network call
                val results = service.weatherFetchAPI(query, units)
                Result.success(results)
            } catch (e: Exception) {
                // Failure result
                Result.failure(e)
            }
        }
}