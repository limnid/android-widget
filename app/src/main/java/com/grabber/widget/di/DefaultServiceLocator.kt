package com.grabber.widget.di

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
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
import com.grabber.widget.presentation.events.EventFactory
import com.grabber.widget.presentation.presenters.MainPresenter
import com.grabber.widget.presentation.presenters.MainPresenterImpl
import com.grabber.widget.presentation.presenters.WidgetPresenter
import com.grabber.widget.presentation.presenters.WidgetPresenterImpl
import com.grabber.widget.services.WidgetWorkerFactory
import com.grabber.widget.widgets.ListRemoteViewsFactory
import java.lang.ref.WeakReference
import java.util.concurrent.Executor
import java.util.concurrent.Executors

open class DefaultServiceLocator(private val app: WidgetApplication, private val inMemory: Boolean) : ServiceLocator {

    private var weakContext: WeakReference<Context>? = null

    init {
        weakContext = WeakReference(app.applicationContext)
    }

    @Suppress("PrivatePropertyName")
    private val DISK_IO = Executors.newSingleThreadExecutor()

    @Suppress("PrivatePropertyName")
    private val NETWORK_IO = Executors.newFixedThreadPool(5)

    private val db by lazy {
        if (inMemory) {
            Room.inMemoryDatabaseBuilder(app.applicationContext!!, WidgetDatabase::class.java)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        } else {
            Room.databaseBuilder(app.applicationContext!!, WidgetDatabase::class.java, "widget.db")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }

    override fun getWidgetPresenter(activity: FragmentActivity): WidgetPresenter {
        return ViewModelProviders.of(activity).get(WidgetPresenterImpl::class.java)
    }

    override fun getWidgetPresenter(fragment: Fragment): WidgetPresenter {
        return ViewModelProviders.of(fragment).get(WidgetPresenterImpl::class.java)
    }

    override fun getMainPresenter(fragment: Fragment): MainPresenter {
        return ViewModelProviders.of(fragment).get(MainPresenterImpl::class.java)
    }

    override fun getRemoteViewsService(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return ListRemoteViewsFactory(app.applicationContext, intent)
    }

    override fun getFeedExecutor(): FeedExecutor = FeedExecutor()
    override fun eventBus() = EventFactory.instance()
    override fun getFeedParser() = FeedParser()
    override fun widgetWorkerFactory(): WidgetWorkerFactory = WidgetWorkerFactory()
    override fun getXmlParser(): FeedParser = FeedParser()
    override fun getRequest(): Request = Request()
    override fun getDataRepository(): DataRepository = DataRepository(getDatabase())
    override fun getDatabase(): WidgetDatabase = db
    override fun getFonts(): Fonts = Fonts(app.applicationContext)
    override fun getNetworkExecutor(): Executor = NETWORK_IO
    override fun getDiskIOExecutor(): Executor = DISK_IO
}