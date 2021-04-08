package com.onurcankoroglu.weatherforecastapp.ui.nearbycities

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurcankoroglu.weatherforecastapp.data.model.Location
import com.onurcankoroglu.weatherforecastapp.network.MetaWeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NearbyCitiesViewModel : ViewModel() {

    private var callSearchLocations: Call<List<Location>>? = null
    private val locations = MutableLiveData<List<Location>>()
    private val progressBar = MutableLiveData<Boolean>()
    var internetConnection = MutableLiveData<Boolean>()

    fun getProgressBar(): LiveData<Boolean> {
        return progressBar
    }

    fun getLocations(): LiveData<List<Location>> {
        return locations
    }

    fun search(txt: String) {
        progressBar.postValue(true)
        callSearchLocations = MetaWeatherService.instance.searchLocation(txt)
        callSearchLocations?.enqueue(object : Callback<List<Location>> {
            override fun onFailure(call: Call<List<Location>>, throwable: Throwable) {
                progressBar.postValue(false)
                when {
                    call.isCanceled -> {
                        Log.i(TAG, "Request cancelled")
                        internetConnection.value = true
                    }
                    throwable is IOException -> {
                        Log.i(TAG, "No internet")
                        internetConnection.value = true
                    }
                    else -> {
                        Log.i(TAG, "Unknown error")
                        internetConnection.value = true
                    }
                }
            }

            override fun onResponse(
                call: Call<List<Location>>,
                response: Response<List<Location>>
            ) {
                progressBar.postValue(false)
                locations.postValue(response.body())
                internetConnection.value = false
            }
        })
    }

    companion object {
        val TAG = NearbyCitiesViewModel::class.java.simpleName
    }

}