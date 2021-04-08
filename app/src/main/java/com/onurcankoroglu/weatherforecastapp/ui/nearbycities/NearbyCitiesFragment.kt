package com.onurcankoroglu.weatherforecastapp.ui.nearbycities

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
import com.onurcankoroglu.weatherforecastapp.databinding.FragmentNearbyCitiesBinding
import com.onurcankoroglu.weatherforecastapp.ui.common.LocationSharedViewModel
import com.onurcankoroglu.weatherforecastapp.ui.searchlocation.LocationAdapter
import com.onurcankoroglu.weatherforecastapp.util.extensions.setVisibility
import kotlinx.android.synthetic.main.fragment_nearby_cities.*

class NearbyCitiesFragment : Fragment() {

    private val nearbyCitiesViewModel: NearbyCitiesViewModel by activityViewModels()
    private val locationSharedViewModel: LocationSharedViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentUserLocation: String
    private lateinit var binding: FragmentNearbyCitiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding = FragmentNearbyCitiesBinding.inflate(inflater, container, false)
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
                locationSharedViewModel.selectedLocation(mwLocationID)
                findNavController().navigate(R.id.action_nearbyCitiesFragment_to_locationInfoFragment)
            }
        })
        binding.fragmentNearbyCitiesRvLocationList.adapter = adapter
        nearbyCitiesViewModel.getLocations().observe(viewLifecycleOwner, Observer { list ->
            adapter.submitList(list)
        })
    }

    private fun setProgressBarVisibility() {
        nearbyCitiesViewModel.getProgressBar().observe(viewLifecycleOwner, Observer {
            fragmentNearbyCities_progressBar.setVisibility(it)
            fragmentNearbyCities_seperator.setVisibility(it.not())
        })
    }

    private fun checkInternetConnection() {
        nearbyCitiesViewModel.internetConnection.observe(viewLifecycleOwner, Observer {
            if(it) {
                findNavController().navigate(R.id.action_nearbyCitiesFragment_to_networkWarningFragment)
            }
        })
    }

    private fun getLastKnownLocation() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                @SuppressLint("MissingPermission")
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    currentUserLocation =
                                        location.latitude.toString() + "," + location.longitude.toString()
                                    nearbyCitiesViewModel.search(currentUserLocation)
                                } else {
                                    findNavController().navigate(R.id.action_nearbyCitiesFragment_to_locationWarningFragment)
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
