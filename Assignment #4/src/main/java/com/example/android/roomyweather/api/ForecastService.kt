package com.example.android.roomyweather.api

import com.example.android.roomyweather.data.ForecastDataAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Setup RetroFit Service to connect with API
interface ForecastService {
    // Setup a function to mirror API setup via asynchronous
    @GET("/data/2.5/forecast")
    suspend fun weatherFetchAPI(
        @Query("q") cityQuery: String?,
        @Query("units")unitsMeasure: String?,
        @Query("appid")userID: String = "affcfb1469412870165a706ade841fd7"
    ) : ForecastDataAPI

    // Setup a companion object live as part of a class
    companion object {
        // Setup a const API URL & a create function
        private const val BASE_URL = "https://api.openweathermap.org"
        fun create() : ForecastService {
            // Create an instance of RetroFit w/ Moshi to parse
            val moshiCreator = Moshi
                .Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val retroFit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshiCreator))
                .build()

            // Return the create interface to generate network service
            return retroFit.create(ForecastService::class.java)
        }
    }
}