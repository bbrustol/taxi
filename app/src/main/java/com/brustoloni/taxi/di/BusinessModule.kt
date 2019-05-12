package com.brustoloni.taxi.di

import com.brustoloni.taxi.business.MapPOIVehiclesBusiness
import org.koin.dsl.module.module

val businessModule = module {

    factory { MapPOIVehiclesBusiness(get()) }
}
