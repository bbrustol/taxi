package com.brustoloni.taxi.data.di

import com.brustoloni.taxi.data.provider.POIVehiclesProvider
import org.koin.dsl.module.module

val provideModule = module(override = true) {
    factory { POIVehiclesProvider(get()) }
}