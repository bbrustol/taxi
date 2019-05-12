package com.brustoloni.taxi.data.entity.map.response

import com.brustoloni.taxi.data.entity.map.Poi
import com.squareup.moshi.Json

data class POIListVehiclesResponse(
    @Json(name = "poiList")
    val poiList: List<Poi>
)