package com.brustoloni.taxi.presentation

import android.app.Application
import com.brustoloni.taxi.data.di.*
import com.brustoloni.taxi.di.businessModule
import com.brustoloni.taxi.di.viewModelModule
import org.koin.android.ext.android.startKoin


class TaxiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(
            this, listOf(
                viewModelModule,
                businessModule,
                provideModule,
                serviceModule,
                networkModule,
                urlModule,
                systemModule
            )
        )
    }
}