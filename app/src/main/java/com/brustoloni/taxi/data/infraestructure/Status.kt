package com.brustoloni.taxi.data.infraestructure

sealed class NetworkState
class DataNotAvailable : NetworkState()
class UnexpectedError : NetworkState()
