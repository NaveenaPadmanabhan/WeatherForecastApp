package com.navy.weatherforecastapp.service

import com.navy.weatherforecastapp.Utils
import com.navy.weatherforecastapp.modeldata.ForeCastData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
/**
 * @Author naveenap
 */

interface Service {

    @GET("weather?")
    fun getTodayWeather(
        @Query("lat")
        lat : String,
        @Query("lon")
        lon : String,
        @Query("appid")
        appid : String =Utils.API_KEY
    ): Call<ForeCastData>

    @GET("weather?")
    fun getTodayWeatherByCity(
        @Query("q")
        city: String,
        @Query("appid")
        appid: String = Utils.API_KEY

    ): Call<ForeCastData>
}