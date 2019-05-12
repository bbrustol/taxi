package com.brustoloni.taxi.data.entity.map


import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Poi(
    @Json(name = "coordinate")
    val coordinate: Coordinate,
    @Json(name = "fleetType")
    val fleetType: String,
    @Json(name = "heading")
    val heading: Double,
    @Json(name = "id")
    val id: Int
) : Parcelable