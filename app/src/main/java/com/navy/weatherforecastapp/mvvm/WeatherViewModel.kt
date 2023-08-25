package com.navy.weatherforecastapp.mvvm

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navy.weatherforecastapp.activity.MyApplication
import com.navy.weatherforecastapp.activity.SharedPreferencesData
import com.navy.weatherforecastapp.modeldata.ForeCastData
import com.navy.weatherforecastapp.service.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * @Author naveenap
 */

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel : ViewModel() {

    val context = MyApplication.instance
    val cityName = MutableLiveData<String>()
    val foreCastDataLiveData = MutableLiveData<ForeCastData>()

    /**
     * get today weather through API
     */
    fun getTodayWeather(city: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        val sharedPrefs = SharedPreferencesData.getInstance(context)
        val lat = sharedPrefs.getValue("lat").toString()
        val lon = sharedPrefs.getValue("lon").toString()

        Log.e("ViewModel", "$lat $lon")

        val call = if (city != null) {
            RetrofitInstance.api.getTodayWeatherByCity(city)
        } else {
            RetrofitInstance.api.getTodayWeather(lat, lon)
        }

        val response = call.execute()

        if (response.isSuccessful) {
            val weatherList = response.body()
            foreCastDataLiveData.postValue(weatherList)

            //println("data----" + weatherList.toString())

            cityName.postValue(response.body()?.name)

        } else {
            val errorMessage = response.message()
            Log.e("CurrentWeatherError", "Error: $errorMessage")
            //Toast.makeText(context, "City not found", Toast.LENGTH_SHORT).show()

        }
    }

}

