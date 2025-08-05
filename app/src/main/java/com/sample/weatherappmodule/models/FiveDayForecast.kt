package com.sample.weatherappmodule.model

data class FiveDayForecast(
    val DailyForecasts: List<DailyForecast>
)

data class DailyForecast(
    val Date: String,
    val Temperature: TemperatureRange,
    val Day: IconInfo,
    val Night: IconInfo
)

data class TemperatureRange(
    val Minimum: TemperatureDetail,
    val Maximum: TemperatureDetail
)

data class TemperatureDetail(
    val Value: Double,
    val Unit: String
)

data class IconInfo(
    val Icon: Int,
    val IconPhrase: String
)
