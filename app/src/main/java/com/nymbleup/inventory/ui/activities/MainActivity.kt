package com.nymbleup.inventory.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.nymbleup.inventory.R
import com.nymbleup.inventory.databinding.ActivityMainBinding
import com.nymbleup.inventory.models.MyEvent
import com.nymbleup.inventory.ui.dialogs.SelectStoreDialog
import com.nymbleup.inventory.ui.fragments.OnActivityInteractionListener
import com.nymbleup.inventory.ui.viewModels.MainViewModel
import com.nymbleup.inventory.ui.viewModels.SupportDataViewModel
import com.nymbleup.inventory.utils.MyMessage
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity(), OnActivityInteractionListener {

    companion object {

        private const val PERMISSION_CALLBACK_CONSTANT = 100
        private const val REQUEST_CHECK_SETTINGS = 101
        private const val REQUESTING_LOCATION_UPDATES_KEY = "requesting"

        private const val TAG = "Main"

        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }

    private var requestingLocationUpdates = false
    private var locationRequest: LocationRequest? = null

    @SuppressLint("InlinedApi")
    private val permissionsRequired = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
    )

    private val dataViewModel: SupportDataViewModel by viewModels()

    private lateinit var context: Context
    private val mainVewModel: MainViewModel by viewModels()

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private lateinit var locationCallback: LocationCallback

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        context = this

        mainVewModel.init(context)
        dataViewModel.init(context, mainVewModel.scheduleApiRepository)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.navView.setupWithNavController(navController)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                mainVewModel.mLocation.postValue(locationResult.locations[0])

                for (location in locationResult.locations) {
                    Log.d(TAG,"update: ${location.latitude}x${location.longitude}")
                }

            }
        }

        updateValuesFromBundle(savedInstanceState)

//        if (askLocationPermission()) {
//            createLocationRequest()
//        }

//        binding.navView.selectedItemId = R.id.navigation_profile

        if (dataViewModel.mSelectedStore.value != null) {
            val store = dataViewModel.mSelectedStore.value
            Log.e(TAG,"store >> ${store?.name} || ${store?.id} || ${store?.storeId}")
        }else {
            Log.e(TAG,"store >> NULL")
        }

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        if (dataViewModel.getStoreId().isBlank()) {
            SelectStoreDialog.show(supportFragmentManager, false)
            return
        }

    }

    private fun updateValuesFromBundle(savedInstanceState: Bundle?) {
        savedInstanceState ?: return
        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY)
        }
    }

    override fun askLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                permissionsRequired[0]
            ) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                context,
                permissionsRequired[1]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Check location!")
            builder.setMessage("Allow location permission to auto detect your location")
            builder.setPositiveButton(
                "Allow"
            ) { dialog, _ ->
                dialog.cancel()
                ActivityCompat.requestPermissions(
                    this,
                    permissionsRequired,
                    PERMISSION_CALLBACK_CONSTANT
                )
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
            builder.show()
            return false
        }
        getLocation()
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allGranted = false
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allGranted = true
                } else {
                    allGranted = false
                    break
                }
            }
            if (allGranted) {
                Log.e("permission", "all permissions granted!")
                createLocationRequest()
                getLocation()
            } else {
                Log.e("permission", "permissions denied")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun getLocation() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            askLocationPermission()
            return
        }

        Log.e(TAG,"getLocation")

        fusedLocationClient?.lastLocation
                ?.addOnSuccessListener { location: Location? ->
                    Log.e("MAIN", "location: ${location?.latitude} x ${location?.longitude} = ${location?.altitude} x ${location?.accuracy}")
                    location?.let {
                        EventBus.getDefault().post(MyEvent(it))
                    }
                }
    }

    private fun createLocationRequest() {

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            requestingLocationUpdates = true
            Log.e(TAG,"LOCATION UPDATE SET")
        }
        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this@MainActivity, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
            Log.e(TAG,"LOCATION UPDATE ERROR -> ${exception.message}")
        }
    }


    override fun onResume() {
        super.onResume()
        if (requestingLocationUpdates) startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            MyMessage.showToast(context, "Location permission not accepted!")
            return
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, requestingLocationUpdates)
        super.onSaveInstanceState(outState)
    }

}