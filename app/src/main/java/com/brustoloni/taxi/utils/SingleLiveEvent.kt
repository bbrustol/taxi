package com.brustoloni.taxi.utils

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    val mPending = AtomicBoolean(false)

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }
}

@MainThread
fun <T> SingleLiveEvent<T>.doObserve(owner: LifecycleOwner, observer: Observer<T>) {

    if (hasActiveObservers()) {
        Log.w(javaClass.canonicalName, "Multiple observers registered but only one will be notified of changes.")
    }

    // Observe the internal MutableLiveData
    observe(owner, Observer { t ->
        if (mPending.compareAndSet(true, false)) {
            observer.onChanged(t)
        }
    })
}