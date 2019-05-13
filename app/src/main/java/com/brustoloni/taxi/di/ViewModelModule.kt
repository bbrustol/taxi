package com.brustoloni.taxi.di

import com.brustoloni.taxi.data.infraestructure.UI
import com.brustoloni.taxi.presentation.map.poilist.POIListViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel { POIListViewModel(get(), get(), UI) }

}

