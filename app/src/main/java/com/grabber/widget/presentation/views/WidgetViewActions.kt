package com.grabber.widget.presentation.views

import com.grabber.widget.data.database.FeedObj

interface WidgetViewActions {
    fun onSubmit(feed: FeedObj?)
}