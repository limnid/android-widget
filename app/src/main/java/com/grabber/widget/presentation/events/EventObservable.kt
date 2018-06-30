package com.grabber.widget.presentation.events

import android.os.Bundle

interface EventObservable {
    fun onEvent(bundle: Bundle?)
}