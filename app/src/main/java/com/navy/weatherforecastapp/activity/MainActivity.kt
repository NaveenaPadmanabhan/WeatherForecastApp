package com.navy.weatherforecastapp.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.navy.weatherforecastapp.R
import com.navy.weatherforecastapp.Utils
import com.navy.weatherforecastapp.databinding.ActivityMainBinding
import com.navy.weatherforecastapp.mvvm.WeatherViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @Author naveenap
 */
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    lateinit var viewModelWeather: WeatherViewModel

    private lateinit var binding: ActivityMainBinding


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModelWeather = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModelWeather.getTodayWeather()


        val sharedPrefs = SharedPreferencesData.getInstance(this@MainActivity)
        sharedPrefs.clearCityValue()


        binding.lifecycleOwner = this
        binding.viewmodel = viewModelWeather


        //Set API Data to UIs
        viewModelWeather.foreCastDataLiveData.observe(this, Observer {
            val temperatureFahrenheit = it!!.main?.temp
            val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
            val temperatureFormatted = String.format("%.2f", temperatureCelsius)


            for (i in it.weather) {


                binding.descMain.text = i.description


            }

            binding.tempMain.text = "$temperatureFormatted°"


            binding.humidityMain.text = it.main!!.humidity.toString()
            binding.windSpeed.text = it.wind?.speed.toString()


            // 2017-10-08T14:35:44.000Z
            val sunriseTimestamp = it.sys.sunrise.toLong()
            val sunsetTimestamp = it.sys.sunset.toLong()
            val sunrise = convertTime(sunriseTimestamp)
            val sunset = convertTime(sunsetTimestamp)
            val dateanddayname = convertDate(it.dt!!.toLong())

            binding.dateDayMain.text = dateanddayname

            binding.chanceofrain.text = "${it.pop.toString()}%"
            binding.sunriseTextView.text = sunrise
            binding.sunsettextview.text = sunset


            // setting the icon
            for (i in it.weather) {


                if (i.icon == "01d") {


                    binding.imageMain.setImageResource(R.drawable.oned)

                }

                if (i.icon == "01n") {
                    binding.imageMain.setImageResource(R.drawable.onen)


                }

                if (i.icon == "02d") {

                    binding.imageMain.setImageResource(R.drawable.twod)


                }


                if (i.icon == "02n") {
                    binding.imageMain.setImageResource(R.drawable.twon)


                }


                if (i.icon == "03d" || i.icon == "03n") {


                    binding.imageMain.setImageResource(R.drawable.threedn)


                }



                if (i.icon == "10d") {

                    binding.imageMain.setImageResource(R.drawable.tend)


                }


                if (i.icon == "10n") {

                    binding.imageMain.setImageResource(R.drawable.tenn)


                }


                if (i.icon == "04d" || i.icon == "04n") {


                    binding.imageMain.setImageResource(R.drawable.fourdn)


                }


                if (i.icon == "09d" || i.icon == "09n") {


                    binding.imageMain.setImageResource(R.drawable.ninedn)


                }



                if (i.icon == "11d" || i.icon == "11n") {


                    binding.imageMain.setImageResource(R.drawable.elevend)


                }


                if (i.icon == "13d" || i.icon == "13n") {

                    binding.imageMain.setImageResource(R.drawable.thirteend)


                }

                if (i.icon == "50d" || i.icon == "50n") {


                    binding.imageMain.setImageResource(R.drawable.fiftydn)


                }

            }
        })
        // Check for location permissions
        if (checkLocationPermissions()) {
            // Permissions are granted, proceed to get the current location
            getCurrentLocation()
        } else {
            // Request location permissions
            requestLocationPermissions()
        }


        val searchEditText =
            binding.searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.WHITE)

        //search Listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {


            override fun onQueryTextSubmit(query: String?): Boolean {


                val sharedPrefs = SharedPreferencesData.getInstance(this@MainActivity)
                sharedPrefs.setValueOrNull("city", query!!)


                if (!query.isNullOrEmpty()) {

                    viewModelWeather.getTodayWeather(query)




                    binding.searchView.setQuery("", false)
                    binding.searchView.clearFocus()
                    binding.searchView.isIconified = true
                }


                return true


            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true


            }


        })


    }

    //check location permissions
    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    // Function to request location permissions
    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            Utils.PERMISSION_REQUEST_CODE
        )
    }

    // Handle the permission request result
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if (requestCode == Utils.PERMISSION_REQUEST_CODE) {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permissions granted, get the current location
                    getCurrentLocation()
                } else {
                    // Permissions denied, handle accordingly
                    // For example, show an error message or disable location-based features
                }
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    // Function to get the current location
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentLocation() {
        try {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val location: Location? =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // Use the latitude and longitude values as needed
                    // ...

                    val myprefs = SharedPreferencesData(this)
                    myprefs.setValue("lon", longitude.toString())
                    myprefs.setValue("lat", latitude.toString())


                    // Example: Display latitude and longitude in logs


                    //Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT).show()


                    Log.d("Current Location", "Latitude: $latitude, Longitude: $longitude")

                    // Reverse geocode the location to get address information
                    reverseGeocodeLocation(latitude, longitude)
                } else {
                    // Location is null, handle accordingly
                    // For example, request location updates or show an error message
                }
            } else {
                // Location permission not granted, handle accordingly
                // For example, show an error message or disable location-based features
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    // Function to reverse geocode the location and get address information
    @RequiresApi(Build.VERSION_CODES.O)
    private fun reverseGeocodeLocation(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val addressLine = address.getAddressLine(0)
                // Use the addressLine as needed
                // ...
                // Example: Display address in logs
                viewModelWeather.getTodayWeather()
                Log.d("Current Address", addressLine)
            } else {
                // No address found, handle accordingly
                // For example, show an error message or use default address values
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    /**
     * convert UTC value to Time
     */
    fun convertTime(time: Long): String? {
        val HMM = SimpleDateFormat("hh:mm a", Locale.US)
        println("UTC Timestamp: $time")
        val date = Date(time * 1000)
        val formattedLocalTime = HMM.format(date)
        println("User's       Local Time: $formattedLocalTime")
        return HMM.format(date)

    }

    /**
     * convert UTC value to Date
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDate(time: Long): String? {
        val dttt = Instant.ofEpochSecond(time)
        val outputFormat = DateTimeFormatter.ofPattern("d MMMM EEEE").withZone(ZoneOffset.UTC)
        val dateanddayname = outputFormat.format(dttt)
        return dateanddayname
    }

}




