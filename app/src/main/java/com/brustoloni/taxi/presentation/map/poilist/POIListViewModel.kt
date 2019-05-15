package com.brustoloni.taxi.presentation.map.poilist

import android.app.Application
import android.view.View
import android.view.View.GONE
import androidx.lifecycle.MutableLiveData
import com.brustoloni.taxi.business.MapPOIVehiclesBusiness
import com.brustoloni.taxi.data.entity.map.Poi
import com.brustoloni.taxi.data.entity.map.response.POIListVehiclesResponse
import com.brustoloni.taxi.data.infraestructure.Failure
import com.brustoloni.taxi.data.infraestructure.Success
import com.brustoloni.taxi.data.infraestructure.handle
import com.brustoloni.taxi.presentation.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class POIListViewModel(
    private val mapPOIVehiclesBusiness: MapPOIVehiclesBusiness,
    application: Application,
    private val coroutineScope: CoroutineScope
) : BaseViewModel(application) {

    private var mLat1:Double = 0.0
    private var mLon1:Double = 0.0
    private var mLat2:Double = 0.0
    private var mLon2:Double = 0.0

    val dataReceived: MutableLiveData<List<Poi>> = MutableLiveData()
    val listVisibility = MutableLiveData<Int>().apply { value = GONE }
    val flagFirstLoad = MutableLiveData<Boolean>().apply { value = false }
    var dataResponse: POIListVehiclesResponse? = null
    fun start(lat1: Double, lon1: Double, lat2: Double, lon2: Double) {
        mLat1 = lat1
        mLon1 = lon1
        mLat2 = lat2
        mLon2 = lon2


        configVisibility(ViewState.LOADING)
        coroutineScope.launch {
            val resource = mapPOIVehiclesBusiness.fetchPOIVehicles(lat1, lon1, lat2, lon2)
            resource.handle(success(), failure())
        }
    }

    private fun failure(): Failure.() -> Unit = {
        configVisibility(ViewState.ERROR)
    }

    private fun success(): Success<POIListVehiclesResponse>.() -> Unit = {

        if (!this.data.poiList.isNullOrEmpty()) {
            dataResponse = this.data
            dataReceived.value = this.data.poiList
            configVisibility(ViewState.SUCCESS)
        } else {
            configVisibility(ViewState.NO_DATA)
        }
    }

    override fun configVisibility(viewState: ViewState) {
        super.configVisibility(viewState)
        val result = setupViewState(viewState)
        listVisibility.value = result.showData
    }

    override fun tryAgain() {
        start(mLat1, mLon1, mLat2, mLon2)
    }

    fun onClickRefresh(view: View) {
        start(mLat1, mLon1, mLat2, mLon2)
    }
}