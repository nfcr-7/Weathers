package training.androidkotlin.weather.city

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import training.androidkotlin.weather.R
import training.androidkotlin.weather.weather.WeatherActivity
import training.androidkotlin.weather.weather.WeatherFragment

class MainActivity : AppCompatActivity(), CityFragment.CityFragmentListener {

    private lateinit var cityFragment: CityFragment

    private var weatherFragment: WeatherFragment? = null
    private var currentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityFragment = supportFragmentManager.findFragmentById(R.id.city_fragment) as CityFragment
        cityFragment.listener = this


    }

    override fun onCitySelected(city: City) {
        currentCity = city
        if (isHandsetLayout()){
            startweatherActivity(city)
        } else {
            weatherFragment?.updateWeatherForCity(city.name)
        }
    }

    override fun onEmptyCities() {
        weatherFragment?.clearUi()
    }

    private fun isHandsetLayout(): Boolean = weatherFragment == null

    private fun startweatherActivity(city: City) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra(WeatherFragment.EXTRA_CITY_NAME, city.name)
        startActivity(intent)
    }
}