package com.grabber.widget.ui

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.grabber.widget.R
import com.grabber.widget.WidgetApplication
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.di.ServiceLocator
import com.grabber.widget.presentation.presenters.WidgetPresenter
import com.grabber.widget.presentation.views.WidgetView
import com.grabber.widget.presentation.views.WidgetViewActions
import com.grabber.widget.widgets.WidgetListProvider
import kotterknife.bindView

class WidgetFragment : BaseFragment(), WidgetView, LifecycleOwner, WidgetViewActions {

    private var presenter: WidgetPresenter? = null
    private var extras: Bundle? = null
    private var feed: FeedObj? = null
    private var serviceLocator: ServiceLocator? = null

    private val mName: EditText by bindView(R.id.name)
    private val mUrl: EditText by bindView(R.id.url)
    private val mSave: Button by bindView(R.id.save)

    var actions: WidgetViewActions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        extras = arguments
        serviceLocator = WidgetApplication.injector()
        actions = this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serviceLocator?.let {
            presenter = it.getWidgetPresenter(this)
            presenter?.setDataRepository(it.getDataRepository())
        }

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val widgetId = extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: 0

        presenter?.findFeedByWidgetId(widgetId, this, Observer {
            it?.let {
                mName.setText(it.name)
                mUrl.setText(it.link)
                feed = it
            }
        })

        mSave.setOnClickListener {
            when {
                mName.text.length < 3 -> {
                    mName.error = getString(R.string.fill_field)
                }
                !Patterns.WEB_URL.matcher(mUrl.text).matches() -> {
                    mUrl.error = getString(R.string.incorrect_url)
                }
                widgetId != AppWidgetManager.INVALID_APPWIDGET_ID -> {
                    if (feed == null) {
                        feed = FeedObj(
                                id = 0,
                                name = "",
                                link = "",
                                description = "",
                                widgetId = widgetId,
                                status = "active",
                                type = "default"
                        )
                    }

                    feed?.name = mName.text.toString()
                    feed?.link = mUrl.text.toString()
                    actions?.onSubmit(feed)
                }
            }
        }
    }

    override fun onSubmit(feed: FeedObj?) {
        feed?.let {
            presenter?.saveFeed(feed, this, Observer {
                extras?.let {
                    val widgetIntent = WidgetListProvider.createUpdateIntent(it)
                    activity?.sendBroadcast(widgetIntent)

                    val resultValue = Intent()
                    resultValue.putExtras(it)
                    activity?.setResult(Activity.RESULT_OK, resultValue)
                    activity?.finish()

                    serviceLocator?.eventBus()?.emit(resultValue.extras)
                }

                WidgetApplication.injector().widgetWorkerFactory().createOneTime()
            })
        }
    }
}