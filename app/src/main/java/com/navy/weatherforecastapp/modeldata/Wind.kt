package com.navy.weatherforecastapp.model

import com.google.gson.annotations.SerializedName
/**
 * @Author naveenap
 */

data class Wind(
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)