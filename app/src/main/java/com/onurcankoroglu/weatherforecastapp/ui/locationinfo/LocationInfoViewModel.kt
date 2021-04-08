package com.onurcankoroglu.weatherforecastapp.ui.locationinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurcankoroglu.weatherforecastapp.network.MetaWeatherService
import com.onurcankoroglu.weatherforecastapp.data.model.LocationInfo
import com.onurcankoroglu.weatherforecastapp.network.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LocationInfoViewModel() : ViewModel() {

    private var callLocationInfo: Call<LocationInfo>? = null
    private val locationInfo = MutableLiveData<LocationInfo>()
    private val progressBar = MutableLiveData<Boolean>()
    private var lastMWLocationID:Int? = null
    var internetConnection = MutableLiveData<Boolean>()


    fun getProgressBar(): LiveData<Boolean> {
        return progressBar
    }

    fun getLocationInfo(): LiveData<LocationInfo> {
        return locationInfo
    }

    fun getLocationDetails(mwLocationID: Int) {
        if (callLocationInfo != null) {
            callLocationInfo!!.cancel()
        }
        if(lastMWLocationID != null && lastMWLocationID == mwLocationID){
            return
        }
        progressBar.postValue(true);
        callLocationInfo = RetrofitBuilder.getService().getLocationInfo(mwLocationID)
        callLocationInfo?.enqueue(object : Callback<LocationInfo> {
            override fun onFailure(call: Call<LocationInfo>, t: Throwable) {
                progressBar.postValue(false)
                when {
                    call.isCanceled -> {
                        Log.i(TAG, "Request cancelled")
                        internetConnection.value = true
                    }
                    t is IOException -> {
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
                call: Call<LocationInfo>,
                response: Response<LocationInfo>
            ) {
                progressBar.postValue(false)
                locationInfo.postValue(response.body())
                lastMWLocationID = mwLocationID
                internetConnection.value = false
            }
        })
    }

    companion object {
        val TAG = LocationInfoViewModel::class.java.simpleName
    }

}