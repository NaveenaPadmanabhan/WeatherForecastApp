package com.navy.weatherforecastapp.modeldata

import com.google.gson.annotations.SerializedName
/**
 * @Author naveenap
 */

data class Coord(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
)