package com.onurcankoroglu.weatherforecastapp.ui.locationinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.onurcankoroglu.weatherforecastapp.R
import com.onurcankoroglu.weatherforecastapp.databinding.FragmentLocationInfoBinding
import com.onurcankoroglu.weatherforecastapp.ui.common.LocationSharedViewModel
import com.onurcankoroglu.weatherforecastapp.util.extensions.setVisibility
import kotlinx.android.synthetic.main.fragment_location_info.*

class LocationInfoFragment : Fragment() {

    private val locationInfoViewModel: LocationInfoViewModel by activityViewModels()
    private val locationSharedViewModel: LocationSharedViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        checkInternetConnection()
        val binding = FragmentLocationInfoBinding.inflate(inflater, container, false)

        locationSharedViewModel.getLocations()
            .observe(viewLifecycleOwner, Observer { mwLocationID ->
                locationInfoViewModel.getLocationDetails(mwLocationID)
            })

        locationInfoViewModel.getLocationInfo()
            .observe(viewLifecycleOwner, Observer { mwLocationInfo ->
                binding.apply {
                    binding.mwLocationInfo = mwLocationInfo
                    binding.forecastSize = mwLocationInfo.consolidated_weather.size
                    executePendingBindings()
                }
            })

        locationInfoViewModel.getProgressBar().observe(viewLifecycleOwner, Observer {
            fragmentLocationInfo_progressBar.setVisibility(it)
        })
        return binding.root
    }

    private fun checkInternetConnection() {
        locationInfoViewModel.internetConnection.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(R.id.action_locationInfoFragment_to_networkWarningFragment)
            }
        })
    }
}