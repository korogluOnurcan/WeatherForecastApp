package com.onurcankoroglu.weatherforecastapp.ui.warning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.onurcankoroglu.weatherforecastapp.databinding.FragmentNetworkWarningBinding

class NetworkWarningFragment : Fragment() {
    private lateinit var binding: FragmentNetworkWarningBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNetworkWarningBinding.inflate(inflater, container, false)
        return binding.root
    }

}
