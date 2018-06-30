package com.grabber.widget.presentation.paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj

class FeedDataSourceFactory(private val repository: DataRepository?) : DataSource.Factory<Meta, FeedObj>() {
    val loadingState: MutableLiveData<LoadingState> = MutableLiveData()
    val feedDataSource = MutableLiveData<FeedDataSource>()

    override fun create(): DataSource<Meta, FeedObj> {
        val source = FeedDataSource(repository, loadingState)
        feedDataSource.postValue(source)
        return source
    }
}
