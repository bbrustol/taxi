package com.brustoloni.taxi.presentation.map.poilist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brustoloni.taxi.R
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.databinding.ItemCardMapPoiVehiclesBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class POIListAdapter(val onItemClickAction: (Poi) -> Unit) : ListAdapter<Poi, POIListAdapter.POIViewHandler>(POIVehiclesDiffCallback()) {
    lateinit var myContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): POIViewHandler {

        myContext = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)

        val itemBinding = DataBindingUtil.inflate<ItemCardMapPoiVehiclesBinding>(
            layoutInflater,
            R.layout.item_card_map_poi_vehicles, parent, false
        )

        return POIViewHandler(itemBinding)
    }

    override fun onBindViewHolder(holder: POIViewHandler, position: Int) = holder.bind(getItem(position))

    inner class POIViewHandler(private val binding: ItemCardMapPoiVehiclesBinding) : RecyclerView.ViewHolder(binding.root),
        OnMapReadyCallback {

        private lateinit var map: GoogleMap
        private lateinit var latLng: LatLng

        /** Initialises the MapView by calling its lifecycle methods */
        init {
            with(binding.liteMap) {
                // Initialise the MapView
                onCreate(null)
                // Set the map ready callback to receive the GoogleMap object
                getMapAsync(this@POIViewHandler)
            }
        }

        fun bind(poi: Poi) = with(binding) {
            item = poi
            binding.btnMap.setOnClickListener {
                onItemClickAction(poi)
            }
            executePendingBindings()

            latLng = LatLng(poi.coordinate.latitude, poi.coordinate.longitude)
            binding.liteMap.tag = this
            setMapLocation()
        }

        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(myContext)
            // If map is not initialised properly
            map = googleMap ?: return
            setMapLocation()
        }

        private fun setMapLocation() {
            if (!::map.isInitialized) return
            with(map) {
                moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13f))
                addMarker(MarkerOptions().position(latLng))
                mapType = GoogleMap.MAP_TYPE_NORMAL
            }
        }

        fun clearView() {
            with(map) {
                // Clear the map and free up resources by changing the map type to none
                clear()
                mapType = GoogleMap.MAP_TYPE_NONE
            }
        }
    }

    class POIVehiclesDiffCallback : DiffUtil.ItemCallback<Poi>() {
        override fun areItemsTheSame(oldItem: Poi, newItem: Poi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Poi, newItem: Poi): Boolean {
            return oldItem == newItem
        }
    }
}