package com.brustoloni.taxi.business

import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import com.brustoloni.taxi.data.infraestructuture.Failure
import com.brustoloni.taxi.data.infraestructuture.Resource
import com.brustoloni.taxi.data.infraestructuture.Success
import com.brustoloni.taxi.data.provider.POIVehiclesProvider

class MapPOIVehiclesBusiness(private val poiVehiclesProvider: POIVehiclesProvider) {

    suspend fun fetchPOIVehicles(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Resource<POIListVehiclesResponse> {

        return when (val response: Resource<POIListVehiclesResponse> = poiVehiclesProvider.fetchPOIVehicles(lat1, lon1, lat2, lon2)) {
            is Success -> Success(response.data)
            is Failure -> Failure(response.data, response.networkState)
        }
    }
}