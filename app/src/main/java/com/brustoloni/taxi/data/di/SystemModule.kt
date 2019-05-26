package com.brustoloni.taxi.data.di

import com.squareup.moshi.Moshi
import org.koin.dsl.module.module


val systemModule = module {
    factory("Default") { Moshi.Builder().build() }
}
