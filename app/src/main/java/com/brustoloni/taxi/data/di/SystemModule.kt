package com.brustoloni.taxi.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module


val systemModule = module {

    factory("Default") { Moshi.Builder().build() }
}
