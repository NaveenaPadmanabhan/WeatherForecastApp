package com.navy.weatherforecastapp.modeldata

import com.google.gson.annotations.SerializedName
/**
 * @Author naveenap
 */

data class Clouds(
    @SerializedName("all")
    val all: Int
)