package com.grabber.widget.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import com.grabber.widget.WidgetApplication

class WidgetListService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
        return WidgetApplication.injector().getRemoteViewsService(intent)
    }
}