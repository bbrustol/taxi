package com.brustoloni.taxi.presentation

import android.view.View.VISIBLE
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.brustoloni.taxi.business.MapPOIVehiclesBusiness
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.data.infraestructure.*
import com.brustoloni.taxi.presentation.map.poilist.POIListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PoiViewModelTest {

    @MockK(relaxUnitFun = true)
    private lateinit var business: MapPOIVehiclesBusiness

    @MockK(relaxUnitFun = true)
    private lateinit var observerSuccess: Observer<List<Poi>>

    @MockK(relaxUnitFun = true)
    private lateinit var observerError: Observer<Int>

    @MockK(relaxUnitFun = true)
    private lateinit var application: TaxiApplication

    private val poiListVehiclesResponse = DataMock.createList()

    private val poi = DataMock.createPoi()

    private lateinit var viewModel : POIListViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = POIListViewModel(business, application, UI)
        every { application.getString(any()) } returns " "
    }

    @Test
    fun shouldBeRetriveFirtAccessWithSuccess() {

        viewModel.dataReceived.value = null

        viewModel.dataReceived.observeForever(observerSuccess)

        coEvery { business.fetchPOIVehicles(any(), any(), any(), any()) } returns Success(poiListVehiclesResponse)

        viewModel.start(0.0, 1.1, 2.2, 3.3)

        verify(exactly = 1) { observerSuccess.onChanged(poiListVehiclesResponse.poiList) }
    }

    @Test
    fun shouldBeRetriveFirstAccessWithError() {
        viewModel.alternativePageVisibility.observeForever(observerError)

        coEvery { business.fetchPOIVehicles(0.0, 1.1, 2.2, 3.3) } returns Failure(Error(), DataNotAvailable())

        viewModel.start(0.0, 1.1, 2.2, 3.3)

        viewModel.tryAgain()

        verify(exactly = 2) { observerError.onChanged(VISIBLE) }
    }
}

