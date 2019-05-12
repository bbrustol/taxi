/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brustoloni.taxi.presentation.map.detail

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.brustoloni.taxi.R
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.utils.Constants
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


val TAG: String = DetailFragment::class.java.name

class DetailFragment: Fragment(), OnCameraMoveStartedListener, OnCameraMoveListener, OnCameraMoveCanceledListener, OnCameraIdleListener, OnMapReadyCallback {
    private var currPolylineOptions: PolylineOptions? = null
    private var isCanceled = false
    private var mPoi: Poi? = null
    private lateinit var map: GoogleMap
    private lateinit var binding: com.brustoloni.taxi.databinding.FragmentDetailBinding

//    private val sydneyLocation: CameraPosition = CameraPosition.Builder().
//            target(LatLng(-33.87365, 151.20689))
//            .zoom(15.5f)
//            .bearing(0f)
//            .tilt(25f)
//            .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { bundle ->
            mPoi = bundle.getParcelable(Constants.EXTRA_POI_LIST_VEHICLES)
        }
        binding.lifecycleOwner = this
        binding.executePendingBindings()
    }

    override fun onResume() {
        super.onResume()
        with(binding.mapDetails) {
            onCreate(null)
            getMapAsync(this@DetailFragment)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(context)
        // return early if the map was not initialised properly
        map = googleMap ?: return

        if (!::map.isInitialized) return
        with(map) {
//            moveCamera(CameraUpdateFactory.newLatLngZoom(sydneyLatLng, 13f))

            setOnCameraIdleListener(this@DetailFragment)
            setOnCameraMoveStartedListener(this@DetailFragment)
            setOnCameraMoveListener(this@DetailFragment)
            setOnCameraMoveCanceledListener(this@DetailFragment)

            // We will provide our own zoom controls.
            uiSettings.isZoomControlsEnabled = false
            uiSettings.isMyLocationButtonEnabled = true

            val vehiclePosition = mPoi?.coordinate?.latitude?.let { mPoi?.coordinate?.longitude?.let { it1 -> LatLng(it, it1) } }

            moveCamera(CameraUpdateFactory.newLatLngZoom(vehiclePosition,15f))
            addMarker(vehiclePosition?.let { MarkerOptions().position(it) })

//            changeCamera(CameraUpdateFactory.newCameraPosition(bondiLocation))

            mapType = MAP_TYPE_NORMAL
        }
    }

    private fun changeCamera(update: CameraUpdate, callback: CancelableCallback? = null) {
        map.animateCamera(update, callback)
    }

    override fun onCameraMoveStarted(reason: Int) {
        if (!isCanceled) map.clear()


        var reasonText = "UNKNOWN_REASON"
        currPolylineOptions = PolylineOptions().width(5f)
        when (reason) {
            OnCameraMoveStartedListener.REASON_GESTURE -> {
                currPolylineOptions?.color(Color.BLUE)
                reasonText = "GESTURE"
            }
            OnCameraMoveStartedListener.REASON_API_ANIMATION -> {
                currPolylineOptions?.color(Color.RED)
                reasonText = "API_ANIMATION"
            }
            OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION -> {
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
}