package com.brustoloni.taxi.data.infraestructuture

import com.brustoloni.taxi.data.entity.map.Coordinate
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse

class DataMock {

    companion object {
        fun createList() = POIListVehiclesResponse(
            listOf(createPoi())
        )

        fun createPoi() = Poi(
            createCoordinate(),
            FLEETTYPE,
            HEADING,
            ID
        )
        fun createCoordinate() = Coordinate(
             0.0,
            0.0
        )

        const val FLEETTYPE: String = "TAXI"
        const val HEADING: Double = 0.0
        const val ID: Int = 0
        const val ILLEGAL_ARGUMENT : String = "Illegal argument"
    }
}