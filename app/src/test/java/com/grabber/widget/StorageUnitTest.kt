package com.grabber.widget

import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.data.database.WidgetDatabase
import com.grabber.widget.di.DefaultServiceLocator
import com.grabber.widget.di.ServiceLocator
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class StorageUnitTest {

    private var serviceLocator: ServiceLocator? = null
    private var application: WidgetApplication? = null
    private var database: WidgetDatabase? = null
    private var repository: DataRepository? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        application = RuntimeEnvironment.application.applicationContext as WidgetApplication?
        ServiceLocator.swap(DefaultServiceLocator(application!!, true))
        serviceLocator = WidgetApplication.injector()
        database = serviceLocator?.getDatabase()
        repository = serviceLocator?.getDataRepository()
    }

    @Test
    @Throws(Exception::class)
    fun shouldDataBaseNotBeNull() {
        assertNotNull(repository)
        assertNotNull(database)
    }

    @Test
    @Throws(Exception::class)
    fun readWriteFeeds() {
        val feeds = repository?.findFeeds()
        assertNotNull(feeds)

        val feed = repository?.saveFeed(FeedObj(
                id = 0,
                name = "test",
                description = "test",
                link = "https://stackoverflow.com/feeds/tag?tagnames=android&sort=newest",
                status = "active",
                type = "test",
                widgetId = 0
        ))
        
        assertNotEquals(feed?.id, 0)
    }

    @After
    @Throws
    fun end() {
        database?.close()
    }
}
