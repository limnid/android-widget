package com.grabber.widget.presentation.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj

interface WidgetPresenter {
    fun setDataRepository(repository: DataRepository)
    fun findVacantFeeds(owner: LifecycleOwner, observer: Observer<List<FeedObj>?>)
    fun findFeedByWidgetId(id: Int, owner: LifecycleOwner, observer: Observer<FeedObj?>)
    fun saveFeed(obj: FeedObj, owner: LifecycleOwner, observer: Observer<FeedObj?>)
}