package com.grabber.widget.ui

import android.appwidget.AppWidgetManager
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grabber.widget.R
import com.grabber.widget.WidgetApplication
import com.grabber.widget.components.AndroidUtilities
import com.grabber.widget.di.ServiceLocator
import com.grabber.widget.presentation.adapters.AdapterListener
import com.grabber.widget.presentation.adapters.ListViewAdapterImpl
import com.grabber.widget.presentation.events.EventObservable
import com.grabber.widget.presentation.presenters.MainPresenter
import com.grabber.widget.widgets.WidgetListProvider
import kotterknife.bindView


class MainFragment : BaseFragment(), AdapterListener, EventObservable {

    private var presenter: MainPresenter? = null
    private var listAdapter: ListViewAdapterImpl = ListViewAdapterImpl()
    private val recyclerView: RecyclerView by bindView(R.id.list)
    private var serviceLocator: ServiceLocator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        serviceLocator = WidgetApplication.injector()
        serviceLocator?.eventBus()?.subscribe(this::class.java.name, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        WidgetListProvider.updateAllWidgets(context)

        recyclerView.adapter = listAdapter
        recyclerView.layoutManager = GridLayoutManager(activity, 1)
        recyclerView.setHasFixedSize(true)

        val repository = serviceLocator?.getDataRepository()
        presenter = serviceLocator?.getMainPresenter(this)
        presenter?.setDataRepository(repository)
        presenter?.loadingState(this, Observer {
            listAdapter.setLoadingState(it)
            onAdapterChanged()
        })

        presenter?.findFeeds(this, Observer {
            listAdapter.submitList(it)
        })

        listAdapter.setCallbackListener(this)
    }

    private fun onAdapterChanged() {
        if (listAdapter.itemCount > 1) {
            AndroidUtilities.hideViewsByTag(view as ViewGroup?, "list_empty")
        } else {
            AndroidUtilities.showViewsByTag(view as ViewGroup?, "list_empty")
        }
    }

    override fun onItemSelected(position: Int, view: View?) {
        val item = listAdapter.currentList?.get(position)
        val activityIntent = Intent(context, WidgetActivity::class.java)
        activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, item?.widgetId)
        activityIntent.putExtra(WidgetListProvider.POSITION, position)
        startActivity(activityIntent)
    }

    override fun onEvent(bundle: Bundle?) {
        val widgetId = bundle?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 0) ?: 0
        val position = bundle?.getInt(WidgetListProvider.POSITION, 0) ?: 0
        if (widgetId > 0) {
            presenter?.reload()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceLocator?.eventBus()?.unsubscribe(this::class.java.name)
    }
}