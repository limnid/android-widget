package com.grabber.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.di.DefaultServiceLocator
import com.grabber.widget.di.ServiceLocator
import com.grabber.widget.presentation.views.WidgetViewActions
import com.grabber.widget.ui.WidgetActivity
import com.grabber.widget.ui.WidgetFragment
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@Config(constants = BuildConfig::class)
@RunWith(RobolectricTestRunner::class)
class WidgetActivityUnitTest {

    private var activity: WidgetActivity? = null
    private var fragment: WidgetFragment? = null
    private var application: WidgetApplication? = null

    private val name = "Test"
    private val link = "http://www.example.com"
    private val widgetId = 1

    @Before
    @Throws(Exception::class)
    fun setup() {
        application = RuntimeEnvironment.application.applicationContext as WidgetApplication?
        ServiceLocator.swap(DefaultServiceLocator(application!!, true))

        val intent = Intent()
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

        activity = Robolectric
                .buildActivity(WidgetActivity::class.java, intent)
                .create()
                .start()
                .resume()
                .visible()
                .get()

        fragment = activity
                ?.supportFragmentManager
                ?.findFragmentById(R.id.container) as WidgetFragment?
    }

    @Test
    @Throws(Exception::class)
    fun shouldActivityNotBeNull() {
        assertNotNull(activity)
    }

    @Test
    @Throws(Exception::class)
    fun shouldServiceLocatorNotBeNull() {
        assertNotNull(WidgetApplication.injector())
    }

    @Test
    @Throws(Exception::class)
    fun shouldFragmentNotBeNull() {
        assertNotNull(fragment)
    }

    @Test
    @Throws(Exception::class)
    fun testForm() {
        val nameInput = fragment?.view?.findViewById<EditText>(R.id.name)
        assertNotNull(nameInput)

        val urlInput = fragment?.view?.findViewById<EditText>(R.id.url)
        assertNotNull(urlInput)

        val button = fragment?.view?.findViewById<Button>(R.id.save)
        assertNotNull(button)

        // Title validation
        button?.performClick()
        assertTrue(nameInput?.error?.toString() == activity?.getString(R.string.fill_field))

        nameInput?.setText(name)

        // URL validation
        button?.performClick()
        assertTrue(urlInput?.error?.toString() == activity?.getString(R.string.incorrect_url))

        urlInput?.setText(link)

        fragment?.actions = object : WidgetViewActions {
            override fun onSubmit(feed: FeedObj?) {
                assertTrue(feed?.name == name)
                assertTrue(feed?.link == link)
            }
        }

        button?.performClick()
    }
}
