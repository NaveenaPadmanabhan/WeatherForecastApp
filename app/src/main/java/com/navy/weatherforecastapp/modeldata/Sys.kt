package com.navy.weatherforecastapp.modeldata

import com.google.gson.annotations.SerializedName
/**
 * @Author naveenap
 */

data class Sys(
    @SerializedName("country")
    val country: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("type")
    val type: Int
)