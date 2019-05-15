package com.brustoloni.taxi.data.entity.map.response

import android.os.Parcelable
import com.brustoloni.taxi.data.entity.map.Poi
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class POIListVehiclesResponse(
    @Json(name = "poiList")
    val poiList: List<Poi>
) : Parcelable