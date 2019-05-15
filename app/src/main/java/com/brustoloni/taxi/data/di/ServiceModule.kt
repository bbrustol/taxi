package com.brustoloni.taxi.data.di

import com.brustoloni.taxi.data.infraestructure.ServiceFactory
import org.koin.dsl.module.module

val serviceModule = module(override = true) {
    factory { ServiceFactory(get()).makePOIVehiclesService() }
}
