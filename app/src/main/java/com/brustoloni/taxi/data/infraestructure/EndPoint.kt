package com.brustoloni.taxi.data.infraestructure

import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface POIVehiclesService {
    @GET("/")
    fun fetchPOIVehiclesAsync(@Query("p1Lat") p1Lat : Double,
                              @Query("p1Lon") p1Lon : Double,
                              @Query("p2Lat") p2Lat : Double,
                              @Query("p2Lon") p2Lon : Double): Deferred<Response<POIListVehiclesResponse>>
}