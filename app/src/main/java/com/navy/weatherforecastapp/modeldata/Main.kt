package com.navy.weatherforecastapp.modeldata

import com.google.gson.annotations.SerializedName
/**
 * @Author naveenap
 */

data class Main(
    @SerializedName("feels_like")
    val feels_like: Double,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("pressure")
    val pressure: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("temp_max")
    val temp_max: Double,
    @SerializedName("temp_min")
    val temp_min: Double
)