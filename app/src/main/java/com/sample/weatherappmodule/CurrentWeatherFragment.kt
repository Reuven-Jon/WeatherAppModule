package com.sample.weatherappmodule

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.sample.weatherappmodule.databinding.FragmentCurrentWeatherBinding
import com.sample.weatherappmodule.model.FiveDayForecast
import com.sample.weatherappmodule.network.RetrofitClient
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!
    private val apiKey = "ERQXgFGXYQtLfBufwVrIoClbGhnofuer"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.etCityKey.setText("306633") // Cape Town default
        binding.btnFetch.setOnClickListener {
            val key = binding.etCityKey.text.toString().trim()
            if (key.isNotEmpty()) fetchFiveDayForecast(key)
        }
    }

    private fun fetchFiveDayForecast(cityKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val forecast = RetrofitClient.service.getFiveDayForecast(cityKey, apiKey)
                withContext(Dispatchers.Main) {
                    displayForecast(forecast, cityKey)

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // handle error (e.g. Toast or Snackbar)
                }
            }
        }
    }

    private fun displayForecast(forecast: FiveDayForecast, cityKey: String)
    {
        val container = binding.forecastContainer
        container.removeAllViews()

        val todayIconPhrase = forecast.DailyForecasts.first().Day.IconPhrase
        val iconRes = getWeatherIconRes(todayIconPhrase)
        binding.imgWeatherIcon.setImageResource(iconRes)
        binding.tvCityName.apply {
            text = "Forecast for city key: $cityKey"
            visibility = View.VISIBLE
        }


        val dateFormatter = SimpleDateFormat("EEE", Locale.getDefault())

        forecast.DailyForecasts.forEach { day ->
            val dayName = dateFormatter.format(SimpleDateFormat("yyyy-MM-dd").parse(day.Date)!!)
            val min = day.Temperature.Minimum.Value
            val max = day.Temperature.Maximum.Value

            val heatLevel = when {
                max >= 35 -> "#FF0000"
                max >= 28 -> "#FF5722"
                max >= 22 -> "#FFC107"
                max >= 16 -> "#4CAF50"
                else -> "#2196F3"
            }

            val forecastLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(16)
                setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }


            val icon = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(72, 72)
                setImageResource(getWeatherIconRes(day.Day.IconPhrase))
                setBackgroundColor(Color.GRAY)
            }

            val dayText = TextView(requireContext()).apply {
                text = "$dayName: ${min}°C / ${max}°C - ${day.Day.IconPhrase}"
                setTextColor(Color.BLACK)
                textSize = 16f
                setPadding(8)
            }

            val heatLine = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams((max * 10).toInt(), 12)
                setBackgroundColor(Color.parseColor(heatLevel))
            }

            val textAndBarLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                addView(dayText)
                addView(heatLine)
            }

            forecastLayout.addView(icon)
            forecastLayout.addView(textAndBarLayout)
            container.addView(forecastLayout)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getWeatherIconRes(iconPhrase: String): Int {
        val lower = iconPhrase.lowercase()

        return when {
            lower.contains("sun") && !lower.contains("cloud") -> R.drawable.ic_sunny
            lower.contains("partly") || lower.contains("mostly") -> R.drawable.ic_partly_cloudy
            lower.contains("cloud") -> R.drawable.ic_cloudy
            lower.contains("heavy rain") || lower.contains("heavy") -> R.drawable.ic_heavy_rain
            lower.contains("drizzle") -> R.drawable.ic_drizzle
            lower.contains("storm") || lower.contains("thunder") || lower.contains("lightning") -> R.drawable.ic_storm
            lower.contains("rain") -> R.drawable.ic_heavy_rain // Fallback for light/moderate rain
            else -> R.drawable.ic_cloudy
        }
    }

}
