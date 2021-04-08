package com.onurcankoroglu.weatherforecastapp.data.model

import com.onurcankoroglu.weatherforecastapp.network.MetaWeatherService
import java.text.SimpleDateFormat
import java.util.*

data class Weather(
    val id: Long,
    val weather_state_name: String,
    val weather_state_abbr: String,
    val wind_direction_compass: String,
    val created: String,
    val applicable_date: String,
    val min_temp: Double,
    val max_temp: Double,
    val the_temp: Double,
    val wind_speed: Double,
    val wind_direction: Double,
    val air_pressure: Double,
    val humidity: Int,
    val visibility: Double,
    val predictability: Int)

{
    fun getImageUrl(): String {
        return MetaWeatherService.getImageUrl(weather_state_abbr)
    }

    private fun formattedApplicableDate(): String = formattedApplicableDate("EEE")

    private fun formattedApplicableDate(pattern: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(applicable_date)
        val format = SimpleDateFormat(pattern, Locale.UK)
        return format.format(date!!)
    }

    private fun formattedFullApplicableDate(): String = formattedApplicableDate("EEE, dd MMM yyyy")

    fun formattedApplicableDate(index: Int): String {
        return when (index) {
            1 -> "Tomorrow"
            -1 -> formattedFullApplicableDate()
            else -> formattedApplicableDate()
        }
    }
}