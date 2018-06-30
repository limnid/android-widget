package com.grabber.widget

import android.view.View
import com.grabber.widget.di.DefaultServiceLocator
import com.grabber.widget.di.ServiceLocator
import com.grabber.widget.ui.MainActivity
import com.grabber.widget.ui.MainFragment
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
class MainActivityUnitTest {
    private var activity: MainActivity? = null
    private var fragment: MainFragment? = null
    private var application: WidgetApplication? = null

    @Before
    @Throws(Exception::class)
    fun setup() {
        application = RuntimeEnvironment.application.applicationContext as WidgetApplication?
        ServiceLocator.swap(DefaultServiceLocator(application!!, true))

        activity = Robolectric
                .buildActivity(MainActivity::class.java)
                .create()
                .start()
                .resume()
                .visible()
                .get()

        fragment = activity
                ?.supportFragmentManager
                ?.findFragmentById(R.id.container) as MainFragment?
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
    fun fragmentShouldBeEmpty() {
        val icon = fragment?.view?.findViewById<View>(R.id.error_icon)
        assertNotNull(icon)
        assertTrue(icon?.visibility == View.VISIBLE)
    }
}
