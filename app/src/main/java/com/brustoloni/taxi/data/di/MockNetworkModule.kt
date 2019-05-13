package com.brustoloni.taxi.data.di

import org.koin.dsl.module.module

val mockURLModule = module(override = true) {
    factory { "http://localhost:51384" }
}
