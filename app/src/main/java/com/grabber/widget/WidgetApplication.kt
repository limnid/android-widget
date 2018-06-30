package com.grabber.widget

import android.app.Application
import android.content.Context
import com.grabber.widget.components.Fonts
import com.grabber.widget.di.ServiceLocator

open class WidgetApplication : Application() {

    companion object {
        var appContext: Context? = null
        var fonts: Fonts? = null

        fun injector(): ServiceLocator {
            return ServiceLocator.instance(appContext!!, false)
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        fonts = injector().getFonts()
        injector().widgetWorkerFactory().createPeriodic()
    }
}