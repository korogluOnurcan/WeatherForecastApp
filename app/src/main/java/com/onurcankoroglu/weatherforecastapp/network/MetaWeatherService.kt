package com.onurcankoroglu.weatherforecastapp.network

import com.onurcankoroglu.weatherforecastapp.data.model.Location
import com.onurcankoroglu.weatherforecastapp.data.model.LocationInfo
import com.onurcankoroglu.weatherforecastapp.data.model.Weather
import com.onurcankoroglu.weatherforecastapp.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetaWeatherService {

    companion object {
        val instance: MetaWeatherService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.DOMAIN + "api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(MetaWeatherService::class.java)
        }

        fun getImageUrl(abbr: String): String {
            return BuildConfig.DOMAIN + "static/img/weather/png/" + abbr + ".png"
        }
    }

    @GET("location/search")
    fun searchLocation(@Query("lattlong") lattLong: String): Call<List<Location>>

    @GET("location/{woeid}")
    fun getLocationInfo(@Path("woeid") woeid: Int): Call<LocationInfo>

    @GET("location/{woeid}/{yyyy}/{mm}/{dd}")
    fun getWeatherForDay(
        @Path("woeid") woeid: Int,
        @Path("yyyy") year: Int,
        @Path("mm") month: Int,
        @Path("dd") day: Int
    ): Call<List<Weather>>
}