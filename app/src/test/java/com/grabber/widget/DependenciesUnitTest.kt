package com.grabber.widget

import android.os.Bundle
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.data.rss.Entry
import com.grabber.widget.data.rss.FeedExecutor
import com.grabber.widget.di.DefaultServiceLocator
import com.grabber.widget.di.ServiceLocator
import com.grabber.widget.presentation.events.EventObservable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch

@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class DependenciesUnitTest : EventObservable {

    private var serviceLocator: ServiceLocator? = null
    private var application: WidgetApplication? = null
    private val key = "testKey"
    private val value = "testValue"
    private val latch = CountDownLatch(1)

    @Before
    @Throws(Exception::class)
    fun setup() {
        application = RuntimeEnvironment.application.applicationContext as WidgetApplication?
        ServiceLocator.swap(DefaultServiceLocator(application!!, true))
        serviceLocator = WidgetApplication.injector()
    }

    @Test
    @Throws(Exception::class)
    fun shouldServiceNotBeNull() {
        assertNotNull(serviceLocator)
    }

    @Test
    @Throws(Exception::class)
    fun testEventBus() {
        val bundle = Bundle()
        bundle.putString(key, value)

        serviceLocator?.eventBus()?.subscribe(key, this)
        serviceLocator?.eventBus()?.emit(bundle)
    }

    @Throws(Exception::class)
    override fun onEvent(bundle: Bundle?) {
        assertNotNull(bundle)
        assertEquals(bundle?.get(key), value)
    }

    @Test
    @Throws(Exception::class)
    fun testRssExecutor() {
        val feedExecutor = serviceLocator?.getFeedExecutor()
        feedExecutor?.feedParser = serviceLocator?.getFeedParser()
        feedExecutor?.callback = object : FeedExecutor.CallBack {
            override fun getFeeds(): List<FeedObj>? {
                return listOf(FeedObj(
                        id = 0,
                        name = "test",
                        description = "test",
                        link = "https://stackoverflow.com/feeds/tag?tagnames=android&sort=newest",
                        status = "active",
                        type = "test",
                        widgetId = 0
                ))
            }

            override fun onItem(feed: FeedObj, it: Entry) {
                assertNotNull(feed.name)
                assertNotNull(feed.link)
                assertNotNull(feed.description)
            }

            override fun onComplete() {
                latch.countDown()
            }
        }
        feedExecutor?.execute()
        latch.await()
    }
}
