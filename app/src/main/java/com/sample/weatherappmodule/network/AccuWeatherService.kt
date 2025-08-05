package com.sample.weatherappmodule.network

import com.sample.weatherappmodule.model.WeatherResponseItem
import com.sample.weatherappmodule.model.FiveDayForecast
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuWeatherService {

    @GET("currentconditions/v1/{locationKey}")
    suspend fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String
    ): List<WeatherResponseItem>

    @GET("forecasts/v1/daily/5day/{locationKey}")
    suspend fun getFiveDayForecast(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String,
        @Query("metric") metric: Boolean = true
    ): FiveDayForecast
}
