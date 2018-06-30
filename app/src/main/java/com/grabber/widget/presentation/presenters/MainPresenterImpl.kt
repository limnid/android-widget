package com.grabber.widget.presentation.presenters

import android.arch.lifecycle.*
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.presentation.paging.FeedDataSourceFactory
import com.grabber.widget.presentation.paging.LoadingState
import com.grabber.widget.presentation.paging.Meta
import java.util.concurrent.Executors

class MainPresenterImpl : ViewModel(), MainPresenter {

    private var mRepository: DataRepository? = null
    private var mDataSourceFactory: FeedDataSourceFactory? = null

    private fun <T> liveData(data: T): LiveData<T> {
        val liveData = MediatorLiveData<T>()
        liveData.postValue(data)
        return liveData
    }

    private fun getPagedConfig() = PagedList.Config
            .Builder()
            .setPageSize(6)
            .setEnablePlaceholders(false)
            .build()

    override fun setDataRepository(repository: DataRepository?) {
        mRepository = repository
        mDataSourceFactory = FeedDataSourceFactory(mRepository)
    }

    override fun findFeeds(owner: LifecycleOwner, observer: Observer<PagedList<FeedObj>?>) {
        mDataSourceFactory?.let {
            LivePagedListBuilder<Meta, FeedObj>(it, getPagedConfig())
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .build()
                    .observe(owner, observer)
        }
    }

    override fun loadingState(owner: LifecycleOwner, observer: Observer<LoadingState?>) {
        getFeedSourceFactory()?.loadingState?.observe(owner, observer)
    }

    override fun reload() {
        getFeedSourceFactory()?.feedDataSource?.value?.invalidate()
    }

    override fun getFeedSourceFactory() = mDataSourceFactory
}