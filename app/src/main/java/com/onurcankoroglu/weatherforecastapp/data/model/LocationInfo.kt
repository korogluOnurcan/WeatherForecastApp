package com.onurcankoroglu.weatherforecastapp.data.model

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

data class LocationInfo(
    val consolidated_weather: List<Weather>,
    val time: String,
    val sun_rise: String,
    val sun_set: String,
    val timezone_name: String,
    val parent: Parent,
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String,
    val timezone: String
) {

    private fun format(str: String): String {
        try {
            val slits = str.split(".")
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)
            inputFormat.timeZone = TimeZone.getTimeZone(timezone)

            val outputFormat = SimpleDateFormat("HH:mm", Locale.UK)
            outputFormat.timeZone = inputFormat.timeZone
            return outputFormat.format(inputFormat.parse(slits[0])!!)
        } catch (e: Exception) {
            Log.e(LocationInfo::class.java.simpleName, e.stackTrace.toString());
        }
        return "-"
    }

    fun formattedTime(): String {
        return format(time)
    }

    fun formattedSunriseTime(): String {
        return format(sun_rise)
    }

    fun formattedSunsetTime(): String {
        return format(sun_set)
    }
}