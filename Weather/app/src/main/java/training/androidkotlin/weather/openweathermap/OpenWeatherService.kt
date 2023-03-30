package training.androidkotlin.weather.openweathermap

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "298a1133dcb3d3ac457fd5836fa64483"

interface OpenWeatherService {

    @GET("data/2.5/weather?units=metric&lang=fr")
    fun getWeather(@Query("q") cityName: String,
                   @Query("appid") apikey: String = API_KEY) : Call<WeatherWrapper>
}