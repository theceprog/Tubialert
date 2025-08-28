package com.proj.tubialert.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.proj.tubialert.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // Observe LiveData (same as before)
        homeViewModel.rainText.observe(viewLifecycleOwner, Observer { newRainText ->
            binding.textViewRain.text = newRainText
        })
        homeViewModel.tempText.observe(viewLifecycleOwner, Observer { newTempText ->
            binding.textViewTemp.text = newTempText
        })
        homeViewModel.waterText.observe(viewLifecycleOwner, Observer { newWaterText ->
            binding.textViewWater.text = newWaterText
        })


        /*
        // If using the SensorReadingsDisplay LiveData from the ViewModel
        homeViewModel.sensorReadingsDisplay.observe(viewLifecycleOwner) { readings ->
            binding.textViewRain.text = readings.rain
            binding.textViewTemp.text = readings.temp
            binding.textViewWater.text = readings.water
        }
        */

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Start listening when the view is created and visible
        homeViewModel.startListeningForSensorUpdates()
        // Or homeViewModel.startListeningForSensorUpdatesAsObject()
    }

// No need to explicitly stop listening here if using ViewModel's onCleared()
// The ViewModel's onCleared will handle detaching the listener when the ViewModel
// is no longer needed (e.g., when the Fragment is permanently destroyed).

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}