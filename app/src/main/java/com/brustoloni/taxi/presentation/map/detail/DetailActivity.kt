package com.brustoloni.taxi.presentation.map.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brustoloni.taxi.R
import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import com.brustoloni.taxi.utils.Constants
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

fun detailActivity() = DetailActivity()

class DetailActivity : AppCompatActivity(),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap

    fun getLaunchingIntent(context: Context?, id: Int, response: POIListVehiclesResponse): Intent {
        val extras = Bundle()
        extras.putParcelable(Constants.EXTRA_POI_LIST_VEHICLES, response)
        extras.putInt(Constants.EXTRA_ID_POI, id)

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtras(extras)

        return intent
    }

    private fun getArgumentPoiList(): POIListVehiclesResponse {
        return intent.getParcelableExtra(Constants.EXTRA_POI_LIST_VEHICLES)
    }

    private fun getArgumentIDSeleted(): Int {
        return intent.getIntExtra(Constants.EXTRA_ID_POI, 0)
    }

    private fun selectedMarkerAnimation(selectedLatLng: LatLng) {
        val location: CameraPosition = CameraPosition.Builder().
            target(selectedLatLng)
            .zoom(15.5f)
            .bearing(0f)
            .tilt(25f)
            .build()

        map.animateCamera(CameraUpdateFactory.newCameraPosition(location))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /*Many part of this code I took from google example*/
    override fun onMapReady(googleMap: GoogleMap?) {
        MapsInitializer.initialize(baseContext)
        map = googleMap ?: return

        var seletedLatLng: LatLng? = null
        val boundsBuilder = LatLngBounds.Builder()


        for (poi in getArgumentPoiList().poiList) {
            val vehiclePosition = LatLng(poi.coordinate.latitude, poi.coordinate.longitude)
            boundsBuilder.include(vehiclePosition)
        }

        if (!::map.isInitialized) return
        with(map) {

            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true

            setOnMarkerClickListener { false }
            moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50))
        }
        
        for (poi in getArgumentPoiList().poiList) {
            val vehiclePosition = LatLng(poi.coordinate.latitude, poi.coordinate.longitude)
            val title : String = poi.fleetType
            val snippet = "Heading: ${poi.heading}\n ID: ${poi.id}"
            var icon: BitmapDescriptor?

            if (getArgumentIDSeleted() == poi.id) {
                seletedLatLng = vehiclePosition
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)
            } else {
                icon = if (title == "TAXI") {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                } else {
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                }
            }

            map.addMarker(
                MarkerOptions()
                    .position(vehiclePosition)
                    .title(title)
                    .snippet(snippet)
                    .icon(icon)
                    .infoWindowAnchor(.5f, .0f)
                    .zIndex(1f))

        }

        if (seletedLatLng != null) {
            selectedMarkerAnimation(seletedLatLng)
        }
    }
}
