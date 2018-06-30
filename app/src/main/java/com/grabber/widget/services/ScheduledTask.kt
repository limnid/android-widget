package com.grabber.widget.services

import android.content.Context
import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ScheduledTask(val context: Context) {
    private val weakReference: WeakReference<Context?> = WeakReference(context)

    private val weakContext: Context?
        get() = weakReference.get()

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private var syncHandle: ScheduledFuture<*>? = null

    fun run() {
        syncHandle = scheduler.scheduleAtFixedRate({}, 0, 10, TimeUnit.SECONDS)
    }
}