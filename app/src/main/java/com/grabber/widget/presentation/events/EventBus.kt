package com.grabber.widget.presentation.events

import android.os.Bundle

interface EventBus {
    fun emit(bundle: Bundle?)
    fun subscribe(key: String, observable: EventObservable)
    fun unsubscribe(key: String)
}