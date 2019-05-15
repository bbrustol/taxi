package com.brustoloni.taxi.data.di

import com.brustoloni.taxi.data.infraestructure.RetrofitFactory
import org.koin.dsl.module.module
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module(override = true) {
    factory {
        RetrofitFactory.makeServiceBuilder(get(), MoshiConverterFactory.create())
    }
}


