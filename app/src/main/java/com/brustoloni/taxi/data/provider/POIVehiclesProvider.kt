package com.brustoloni.taxi.data.provider

import com.brustoloni.taxi.data.infraestructuture.POIVehiclesService
import com.brustoloni.taxi.data.infraestructuture.callAsync

class POIVehiclesProvider(private val service: POIVehiclesService) {

    suspend fun fetchPOIVehicles(lat1: Double, lon1: Double, lat2: Double, lon2: Double) =
        callAsync { service.fetchPOIVehiclesAsync(lat1, lon1, lat2, lon2) }.await()
}