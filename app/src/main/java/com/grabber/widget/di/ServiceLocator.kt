package com.grabber.widget.di

import android.content.Context
import android.content.Intent
import android.support.annotation.VisibleForTesting
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.widget.RemoteViewsService
import com.grabber.widget.WidgetApplication
import com.grabber.widget.components.Fonts
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.WidgetDatabase
import com.grabber.widget.data.network.Request
import com.grabber.widget.data.rss.FeedExecutor
import com.grabber.widget.data.rss.FeedParser
import com.grabber.widget.presentation.events.EventBus
import com.grabber.widget.presentation.presenters.MainPresenter
import com.grabber.widget.presentation.presenters.WidgetPresenter
import com.grabber.widget.services.WidgetWorkerFactory
import java.util.concurrent.Executor

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context, inMemory: Boolean): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                            context.applicationContext as WidgetApplication, inMemory)
                }
                return instance!!
            }
        }

        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun eventBus(): EventBus
    fun widgetWorkerFactory(): WidgetWorkerFactory
    fun getXmlParser(): FeedParser
    fun getRequest(): Request
    fun getMainPresenter(fragment: Fragment): MainPresenter
    fun getWidgetPresenter(activity: FragmentActivity): WidgetPresenter
    fun getWidgetPresenter(fragment: Fragment): WidgetPresenter
    fun getDatabase(): WidgetDatabase
    fun getDataRepository(): DataRepository
    fun getFeedParser(): FeedParser
    fun getFeedExecutor(): FeedExecutor
    fun getRemoteViewsService(intent: Intent): RemoteViewsService.RemoteViewsFactory
    fun getNetworkExecutor(): Executor
    fun getDiskIOExecutor(): Executor
    fun getFonts(): Fonts
}