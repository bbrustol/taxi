package com.brustoloni.taxi.data.infraestructuture

sealed class NetworkState
class DataNotAvailable : NetworkState()
class UnexpectedError : NetworkState()
