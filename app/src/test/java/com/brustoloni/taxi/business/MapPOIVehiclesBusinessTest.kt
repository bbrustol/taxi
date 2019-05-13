package com.brustoloni.taxi.business

import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import com.brustoloni.taxi.data.infraestructuture.*
import com.brustoloni.taxi.data.provider.POIVehiclesProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test


class MapPOIVehiclesBusinessTest {

    private lateinit var business: MapPOIVehiclesBusiness

    @MockK(relaxUnitFun = true)
    private lateinit var remoteProvider: POIVehiclesProvider

    private val poiVehiclesResponse = DataMock.createList()

    private val poi = DataMock.createPoi()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        business = MapPOIVehiclesBusiness(remoteProvider)
    }

    @Test
    fun shouldBeRetrieveFromRemote() = runBlocking {

        coEvery { remoteProvider.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3) } returns Success(
            POIListVehiclesResponse(listOf(poi))
        )

        val fetchPoi = business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3)

        fetchPoi.handle({ assertEquals(poiVehiclesResponse, data) })
    }


    @Test
    fun shouldBeErrorFromRemote() = runBlocking {

        coEvery { remoteProvider.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3) } returns Failure(Error(CATEGORY))

        val fetchPoi = business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3)

        fetchPoi.handle({ fail() }, { assertEquals(CATEGORY, data.message) })
    }

    @Test
    fun shouldBeRetrievePoi() = runBlocking {

        coEvery { remoteProvider.fetchPOIVehicles(any(), any(), any(), any()) } returns Success(
            POIListVehiclesResponse(listOf(poi))
        )

        val fetchPoi = business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3)

        fetchPoi.handle({
            assertEquals(poi, data.poiList[0])
            assertEquals(poiVehiclesResponse.poiList[0], data.poiList[0])
        })
    }

    @Test
    fun shouldBeRetrieveErrorPoi() = runBlocking {

        coEvery { remoteProvider.fetchPOIVehicles(any(), any(), any(), any()) } returns Failure(
            Error(
                DataMock.ILLEGAL_ARGUMENT
            )
        )

        val fetchPoi = business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3)

        fetchPoi.handle({ fail() }, { assertEquals(DataMock.ILLEGAL_ARGUMENT, data.message) })
    }

    @Test
    fun shouldBeRetrieveErrorWhenFetchPoi() = runBlocking {

        coEvery { remoteProvider.fetchPOIVehicles(any(), any(), any(), any()) } returns Failure(
            Error(
                DataMock.ILLEGAL_ARGUMENT
            )
        )

        val fetchPoi = business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3)

        fetchPoi.handle({ fail() }, { assertEquals(DataNotAvailable().javaClass, this.networkState.javaClass) })
    }
}