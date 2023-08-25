package com.navy.weatherforecastapp.activity

import android.app.Application

/**
 * @Author naveenap
 */

class MyApplication : Application() {

    companion object {

        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}