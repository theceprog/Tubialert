package com.proj.tubialert.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import androidx.core.graphics.toColorInt

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
            binding.safetext.text = newStatusText

            val colorResId = when (newStatusText?.trim()?.lowercase()) {
                "safe" -> R.color.status_safe_green
                "monitoring" -> R.color.status_monitoring_yellow
                "alert" -> R.color.status_alert_orange
                "evacuate" -> R.color.status_evacuate_red
                else -> R.color.status_default_color
            }
            binding.safetext.setTextColor(ContextCompat.getColor(requireContext(), colorResId))
        })
        homeViewModel.lastUpdateText.observe(viewLifecycleOwner, Observer { text ->
            binding.lastupdate.text = text
        })

        homeViewModel.weatherDescriptionText.observe(viewLifecycleOwner, Observer { text ->
            binding.weather.text = text
        })

        homeViewModel.rainChanceText.observe(viewLifecycleOwner, Observer { text ->
            binding.rainchance.text = text
        })

        homeViewModel.accuWeatherStatus.observe(viewLifecycleOwner, Observer { status ->
            Log.i("HomeFragment", "AccuWeather Status: $status")
        })

        homeViewModel.waterText.observe(viewLifecycleOwner, Observer { newWaterText ->
            binding.textViewWater.text = newWaterText
        })

        homeViewModel.waterCardColor.observe(viewLifecycleOwner, Observer { color ->
            binding.waterlevelcontainer.setCardBackgroundColor(color)
        })

        homeViewModel.weatherImageResId.observe(viewLifecycleOwner, Observer { imageResId ->
            Log.d("HomeFragment", "weatherImageResId Observer triggered with imageResId: $imageResId")
            if (imageResId != 0 && imageResId != null) { // Basic check for valid resource ID
                binding.weatherimage.setImageResource(imageResId)
            } else {
                Log.w("HomeFragment", "Received invalid imageResId: $imageResId, not setting image.")

            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.startListeningForSensorUpdates()
        homeViewModel.fetchAccuWeatherData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}