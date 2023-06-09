package training.androidkotlin.weather.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import training.androidkotlin.weather.App
import training.androidkotlin.weather.R
import training.androidkotlin.weather.openweathermap.WeatherWrapper
import training.androidkotlin.weather.openweathermap.mapOpenWeatherDataToWeather

class WeatherFragment : Fragment() {

    companion object {
        const val EXTRA_CITY_NAME = "training.kotlin.weather.extras.EXTRA_CITY_NAME"

        fun newInstance(): WeatherFragment = WeatherFragment()
    }

    private val TAG = WeatherFragment::class.java.simpleName

    private lateinit var cityName: String

    private lateinit var refreshLayout: SwipeRefreshLayout
    private lateinit var city: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var weatherDescription: TextView
    private lateinit var temperature: TextView
    private lateinit var humidity: TextView
    private lateinit var pressure: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        refreshLayout = view.findViewById(R.id.swipe_refresh)
        city = view.findViewById(R.id.city)
        weatherIcon = view.findViewById(R.id.weather_icon)
        weatherDescription = view.findViewById(R.id.weather_description)
        temperature = view.findViewById(R.id.temperature)
        humidity = view.findViewById(R.id.humidity)
        pressure = view.findViewById(R.id.pressure)

        refreshLayout.setOnRefreshListener { refreshWeather() }


        return view
    }


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        if(activity?.intent!!.hasExtra(EXTRA_CITY_NAME)) {
            updateWeatherForCity(activity!!.intent.getStringExtra(EXTRA_CITY_NAME))
        }
    }

    fun updateWeatherForCity(cityName: String?) {
        this.cityName = cityName!!

        this.city.text = cityName

        if (!refreshLayout.isRefreshing) {
            refreshLayout.isRefreshing = true
        }

        val call = App.weatherService.getWeather("$cityName,fr")
        call.enqueue(object : Callback<WeatherWrapper> {

            override fun onResponse(
                call: Call<WeatherWrapper>,
                response: Response<WeatherWrapper>
            ) {
                Log.i(TAG, "Received weather data ${response.body()}")
                response.body()?.let {
                    val weather = mapOpenWeatherDataToWeather(it)
                    updateUi(weather)
                    Log.i(TAG, "Weather response: $weather")
                    refreshLayout.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<WeatherWrapper>, t: Throwable) {
                Log.e(TAG, "Could not load city      weather", t)
                Toast.makeText(activity,
                getString(R.string.weather_message_error_could_not_load_weather),
                Toast.LENGTH_SHORT).show()
                refreshLayout.isRefreshing = false
            }

        })
    }

    @SuppressLint("PrivateResource")
    private fun updateUi(weather: Weather) {

        Picasso.get()
            .load(weather.iconUrl)
            .placeholder(com.google.android.material.R.drawable.ic_clear_black_24)
            .into(weatherIcon)

        weatherDescription.text = weather.description
        temperature.text = getString(R.string.weather_temperature_value, weather.temperature.toInt())
        humidity.text = getString(R.string.weather_humidity_value, weather.humidity)
        pressure.text = getString(R.string.weather_pressure_value, weather.pressure)
    }

    private fun refreshWeather() {
        updateWeatherForCity(cityName)
    }

    @SuppressLint("PrivateResource")
    fun clearUi() {
        weatherIcon.setImageResource(com.google.android.material.R.drawable.ic_clock_black_24dp)
        cityName = ""
        city.text = ""
        weatherDescription.text = ""
        temperature.text = ""
        humidity.text = ""
        pressure.text = ""
    }

}
