package com.proj.tubialert.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.proj.tubialert.R
import com.proj.tubialert.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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
        homeViewModel.statusText.observe(viewLifecycleOwner, Observer { newStatusText ->
            binding.safetext.text = newStatusText // Set the text first

            val colorResId = when (newStatusText?.trim()?.lowercase()) { // Use lowercase and trim for robust comparison
                "safe" -> R.color.status_safe_green
                "monitoring" -> R.color.status_monitoring_yellow
                "alert" -> R.color.status_alert_orange
                "evacuate" -> R.color.status_evacuate_red
                else -> R.color.status_default_color // Default color if no match
            }
            binding.safetext.setTextColor(ContextCompat.getColor(requireContext(), colorResId))
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Start listening when the view is created and visible
        homeViewModel.startListeningForSensorUpdates()
        // Or homeViewModel.startListeningForSensorUpdatesAsObject()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}