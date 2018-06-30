package com.grabber.widget.presentation.presenters

import android.arch.lifecycle.*
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj
import kotlin.concurrent.thread

class WidgetPresenterImpl : ViewModel(), WidgetPresenter {

    private var mRepository: DataRepository? = null
    private val listData = MutableLiveData<List<FeedObj>?>()
    private val data = MutableLiveData<FeedObj?>()

    private fun <T> liveData(data: T): LiveData<T> {
        val liveData = MediatorLiveData<T>()
        liveData.postValue(data)
        return liveData
    }

    override fun setDataRepository(repository: DataRepository) {
        mRepository = repository
    }

    override fun findVacantFeeds(owner: LifecycleOwner, observer: Observer<List<FeedObj>?>) {
        thread { listData.postValue(mRepository?.findVacantFeed()) }
        listData.observe(owner, observer)
    }

    override fun findFeedByWidgetId(id: Int, owner: LifecycleOwner, observer: Observer<FeedObj?>) {
        thread { data.postValue(mRepository?.findFeedByWidgetId(id)) }
        data.observe(owner, observer)
    }

    override fun saveFeed(obj: FeedObj, owner: LifecycleOwner, observer: Observer<FeedObj?>) {
        thread { data.postValue(mRepository?.saveFeed(obj)) }
        data.observe(owner, observer)
    }
}