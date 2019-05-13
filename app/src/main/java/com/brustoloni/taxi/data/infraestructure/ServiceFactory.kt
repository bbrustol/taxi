package com.brustoloni.taxi.data.infraestructure

import retrofit2.Retrofit

class ServiceFactory(private val retrofit: Retrofit){

    fun makePOIVehiclesService(): POIVehiclesService = retrofit.create(POIVehiclesService::class.java)
}