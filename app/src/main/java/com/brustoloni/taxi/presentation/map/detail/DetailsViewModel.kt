package com.brustoloni.taxi.presentation.map.detail

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.presentation.BaseViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions

class DetailsViewModel (application: Application, private val context: Context) : BaseViewModel(application), GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener,
    GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener, OnMapReadyCallback {

    private var currPolylineOptions: PolylineOptions? = null
    private var isCanceled = false
    private var mPoi: Poi? = null
    private lateinit var map: GoogleMap

    fun start(mapDetails: MapView, poi: Poi?) {
        mPoi = poi
        with(mapDetails) {
            onCreate(null)
            getMapAsync(this@DetailsViewModel)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        // return early if the map was not initialised properly
        map = googleMap ?: return

        if (!::map.isInitialized) return
        with(map) {
            //            moveCamera(CameraUpdateFactory.newLatLngZoom(sydneyLatLng, 13f))

            setOnCameraIdleListener(this@DetailsViewModel)
            setOnCameraMoveStartedListener(this@DetailsViewModel)
            setOnCameraMoveListener(this@DetailsViewModel)
            setOnCameraMoveCanceledListener(this@DetailsViewModel)

            // We will provide our own zoom controls.
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled = true

            val vehiclePosition = mPoi?.coordinate?.latitude?.let { mPoi?.coordinate?.longitude?.let { it1 ->
                com.google.android.gms.maps.model.LatLng(
                    it,
                    it1
                )
            } }

            moveCamera(CameraUpdateFactory.newLatLngZoom(vehiclePosition,15f))
            addMarker(vehiclePosition?.let { com.google.android.gms.maps.model.MarkerOptions().position(it) })

//            changeCamera(CameraUpdateFactory.newCameraPosition(bondiLocation))

            mapType = GoogleMap.MAP_TYPE_NORMAL
        }
    }

    private fun changeCamera(update: CameraUpdate, callback: GoogleMap.CancelableCallback? = null) {
        map.animateCamera(update, callback)
    }

    override fun onCameraMoveStarted(reason: Int) {
        if (!isCanceled) map.clear()

        var reasonText = "UNKNOWN_REASON"
        currPolylineOptions = PolylineOptions().width(5f)
        when (reason) {
            GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE -> {
                currPolylineOptions?.color(Color.BLUE)
                reasonText = "GESTURE"
            }
            GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION -> {
                currPolylineOptions?.color(Color.RED)
                reasonText = "API_ANIMATION"
            }
            GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION -> {
                currPolylineOptions?.color(Color.GREEN)
                reasonText = "DEVELOPER_ANIMATION"
            }
        }
        Log.i(TAG, "onCameraMoveStarted($reasonText)")
        addCameraTargetToPath()
    }

    private fun checkPolylineThen(stuffToDo: () -> Unit) {
        if (currPolylineOptions != null) stuffToDo()
    }

    override fun onCameraMove() {
        Log.i(TAG, "onCameraMove")
        checkPolylineThen { addCameraTargetToPath() }
    }

    override fun onCameraMoveCanceled() {
        checkPolylineThen {
            addCameraTargetToPath()
            map.addPolyline(currPolylineOptions)
        }
        isCanceled = true
        currPolylineOptions = null
        Log.i(TAG, "onCameraMoveCancelled")
    }

    override fun onCameraIdle() {
        checkPolylineThen {
            addCameraTargetToPath()
            map.addPolyline(currPolylineOptions)
        }

        currPolylineOptions = null
        isCanceled = false
        Log.i(TAG, "onCameraIdle")
    }

    private fun addCameraTargetToPath() {
        currPolylineOptions?.add(map.cameraPosition.target)
    }

    override fun tryAgain() { /*Nothing to do*/}
}