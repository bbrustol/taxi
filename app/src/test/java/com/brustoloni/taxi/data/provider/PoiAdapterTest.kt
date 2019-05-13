package com.brustoloni.taxi.data.provider

import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import com.brustoloni.taxi.data.infraestructure.ResourceUtils
import com.squareup.moshi.Moshi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class PoiAdapterTest {

    private val emptyJson = "{\"poiList\": []}"

    private lateinit var moshi: Moshi

    @Before
    fun setUp(){
        moshi = Moshi.Builder()
            .build()
    }

    @Test
    fun shouldBeParse() {
        val json = ResourceUtils().openFile("poi_vehicles_response_200.json")


        val adapter =
            moshi.adapter(POIListVehiclesResponse::class.java)
        val poiResponse = adapter.fromJson(json!!)

        val poi = poiResponse?.poiList?.get(0)

        assertNotNull(poi)

        assertEquals(905952, poi?.id)
        //explicit null value
        assertEquals("TAXI", poi?.fleetType)

        //not send in json
        assertEquals(4.947849997371024, poi?.heading)

        val latlng = poi?.coordinate

        assertEquals(53.624030196194624, latlng?.latitude)
        assertEquals(10.010969151443595, latlng?.longitude)

    }

    @Test
    fun shouldBeReturnEmptyPoi() {

        val adapter =
            moshi.adapter(POIListVehiclesResponse::class.java)

        val poiResponse = adapter.fromJson(emptyJson)

        assertTrue(poiResponse!!.poiList.isEmpty())
    }

    @Test
    fun shouldBeReturnInstrunctionsProdBug() {

        val json = ResourceUtils().openFile("poi_vehicles_response_200.json")

        val adapter =
            moshi.adapter(POIListVehiclesResponse::class.java)

        val poiResponse = adapter.fromJson(json!!)

        assertTrue(poiResponse!!.poiList[0].fleetType.isNotEmpty())
    }

}