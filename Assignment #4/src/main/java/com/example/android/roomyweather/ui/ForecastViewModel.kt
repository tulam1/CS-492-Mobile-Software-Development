package com.example.android.roomyweather.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.roomyweather.api.ForecastService
import com.example.android.roomyweather.data.*
import kotlinx.coroutines.launch

// Create a ViewModel for the UI of the Forecast
class ForecastViewModel : ViewModel() {
    // Setup the repo for forecast & liveData
    private val repo = ForecastRepository(ForecastService.create())
    private val _weatherResults = MutableLiveData<ForecastDataAPI?>(null)
    val weatherResults: LiveData<ForecastDataAPI?> = _weatherResults

    // Setup a status loading event
    private val _loadingStatus = MutableLiveData(ForecastLoadingStatus.SUCCESS)
    val loadingStatus: LiveData<ForecastLoadingStatus> = _loadingStatus

    // Create a function to load the result onto ViewModel & its loading status
    fun loadWeatherResults(query: String?, units: String?) {
        viewModelScope.launch {
            _loadingStatus.value = ForecastLoadingStatus.LOADING
            val resultData = repo.loadRepositoryWeather(query, units)
            _weatherResults.value = resultData.getOrNull()
            _loadingStatus.value = when (resultData.isSuccess) {
                true -> ForecastLoadingStatus.SUCCESS
                false -> ForecastLoadingStatus.ERROR
            }
        }
    }
}