package com.brustoloni.taxi.data.di

import com.brustoloni.taxi.BuildConfig
import org.koin.dsl.module.module

val urlModule = module {
    factory { BuildConfig.BASE_URL }
}