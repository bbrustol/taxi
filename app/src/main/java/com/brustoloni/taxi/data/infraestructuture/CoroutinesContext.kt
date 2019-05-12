package com.brustoloni.taxi.data.infraestructuture

import android.util.Log
import kotlinx.coroutines.*
import retrofit2.Response

val job = SupervisorJob()
val UI = CoroutineScope(Dispatchers.Main + job)

fun <T> callAsync(block: () -> Deferred<Response<T>>) = UI.async {

    try {

        val response = block().await()

        if (response.isSuccessful) {
            Success(response.body()!!)
        } else {
            Log.d(this.javaClass.canonicalName, response.errorBody().toString())
            Failure(Error(response.errorBody().toString()))
        }
    } catch (e: Exception) {
        Failure(Error(e.message), UnexpectedError())
    }
}




