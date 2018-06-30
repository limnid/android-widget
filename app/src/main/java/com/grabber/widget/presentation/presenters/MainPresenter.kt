package com.grabber.widget.presentation.presenters

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.paging.PagedList
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.presentation.paging.FeedDataSourceFactory
import com.grabber.widget.presentation.paging.LoadingState

interface MainPresenter {
    fun findFeeds(owner: LifecycleOwner, observer: Observer<PagedList<FeedObj>?>)
    fun loadingState(owner: LifecycleOwner, observer: Observer<LoadingState?>)
    fun setDataRepository(repository: DataRepository?)
    fun getFeedSourceFactory(): FeedDataSourceFactory?
    fun reload()
}