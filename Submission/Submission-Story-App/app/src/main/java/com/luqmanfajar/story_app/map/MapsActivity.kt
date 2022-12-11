package com.luqmanfajar.story_app.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.luqmanfajar.story_app.R
import com.luqmanfajar.story_app.api.ApiConfig
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.api.StoriesResponse
import com.luqmanfajar.story_app.data.preference.LoginPreferences
import com.luqmanfajar.story_app.data.viewmodel.LoginViewModel
import com.luqmanfajar.story_app.data.preference.PreferencesFactory
import com.luqmanfajar.story_app.dataStore
import com.luqmanfajar.story_app.databinding.ActivityMapsBinding
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_map, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when ( item.itemId){
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun getAllStories(tokenAuth: String){

        val service = ApiConfig().getApiService2(tokenAuth).getStoriesLocation(1,1)
        service.enqueue(object : retrofit2.Callback<StoriesResponse>{
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {

                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val arrayStory: ArrayList<ListStoryItem> = responseBody.listStory as ArrayList<ListStoryItem>
                        addManyMarker(arrayStory)
                        Toast.makeText(this@MapsActivity, "Data Loaded", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(this@MapsActivity, response.message(), Toast.LENGTH_SHORT).show()

                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "Gagal instance Retrofit", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun addManyMarker(data : ArrayList<ListStoryItem>) {

        data.forEach { dataMap ->
            val latLng = LatLng(dataMap.lat, dataMap.lon)
            val addressName = getAddressName(dataMap.lat, dataMap.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(addressName).snippet(addressName))
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
                Log.d("Main", "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }




    override fun onMapReady(googleMap: GoogleMap) {

        val pref = LoginPreferences.getInstance(dataStore)

        val loginViewModel = ViewModelProvider(this, PreferencesFactory(pref)).get(
            LoginViewModel::class.java
        )

        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        loginViewModel.getAuthKey().observe(this
        ){
                authToken : String ->

            getAllStories(authToken!!)
        }
        getMyLocation()

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    companion object {
        const val EXTRA_MAP = "extra_token"
    }
}