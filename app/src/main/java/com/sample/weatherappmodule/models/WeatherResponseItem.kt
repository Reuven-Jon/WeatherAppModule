package com.sample.weatherappmodule.model

data class WeatherResponseItem(
    val WeatherText: String,
    val Temperature: Temperature
)

data class Temperature(
    val Metric: Metric
)

data class Metric(
    val Value: Double,
    val Unit: String
)
