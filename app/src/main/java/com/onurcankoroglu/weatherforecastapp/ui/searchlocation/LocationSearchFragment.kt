package com.onurcankoroglu.weatherforecastapp.ui.searchlocation

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.onurcankoroglu.weatherforecastapp.R
import com.onurcankoroglu.weatherforecastapp.databinding.FragmentLocationSearchBinding
import com.onurcankoroglu.weatherforecastapp.util.extensions.setVisibility
import kotlinx.android.synthetic.main.fragment_location_search.*

class LocationSearchFragment : Fragment() {

    private val locationSearchViewModel: LocationSearchViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: String
    private lateinit var binding: FragmentLocationSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        getLastKnownLocation()
        setAdapterProperties()
        setProgressBarVisibility()
        checkInternetConnection()
    }

    private fun setAdapterProperties() {

        val adapter = LocationAdapter(object : LocationAdapter.InteractionListener {
            override fun onItemSelected(mwLocationID: Int) {
            }
        })
        binding.fragmentLocationSearchRvLocationList.adapter = adapter
        locationSearchViewModel.getLocations().observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })
    }

    private fun setProgressBarVisibility() {
        locationSearchViewModel.getProgressBar().observe(viewLifecycleOwner, Observer {
            fragmentLocationSearch_progressBar.setVisibility(it)
            fragmentLocationSearch_seperator.setVisibility(it.not())
        })
    }

    private fun checkInternetConnection() {
        locationSearchViewModel.internetConnection.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(R.id.action_searchLocationFragment_to_networkWarningFragment)
            }
        })
    }

    private fun getLastKnownLocation() {

        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { location ->
                                    if (location != null) {
                                        currentUserLocation =
                                            location.latitude.toString() + "," + location.longitude.toString()
                                        locationSearchViewModel.search(currentUserLocation)
                                    } else {
                                        findNavController().navigate(R.id.action_searchLocationFragment_to_locationWarningFragment)
                                    }
                                }
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener {
            }
            .check()
    }

}
