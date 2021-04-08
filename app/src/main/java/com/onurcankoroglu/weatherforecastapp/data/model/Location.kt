package com.onurcankoroglu.weatherforecastapp.data.model

data class Location(
    val title: String,
    val location_type: String,
    val woeid: Int,
    val latt_long: String,
    val distance: Int
)