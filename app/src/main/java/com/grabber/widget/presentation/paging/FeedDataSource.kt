package com.grabber.widget.presentation.paging

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.grabber.widget.data.DataRepository
import com.grabber.widget.data.database.FeedObj

class FeedDataSource(
        private val repository: DataRepository?,
        private val loadingState: MutableLiveData<LoadingState>,
        private val meta: Meta = Meta()) : PageKeyedDataSource<Meta, FeedObj>() {

    override fun loadInitial(params: LoadInitialParams<Meta>, callback: LoadInitialCallback<Meta, FeedObj>) {
        loadingState.postValue(LoadingState.DONE)
        val items = repository?.findPagedFeeds(params.requestedLoadSize, 0)
        items?.let {
            callback.onResult(it, Meta(), meta)
            meta.offset += it.size
        }
    }

    override fun loadAfter(params: LoadParams<Meta>, callback: LoadCallback<Meta, FeedObj>) {
        loadingState.postValue(LoadingState.DONE)
        val items = repository?.findPagedFeeds(params.requestedLoadSize, params.key.offset)
        items?.let {
            if (it.isNotEmpty()) {
                callback.onResult(it, params.key)
                params.key.offset += it.size
            }
        }
    }

    override fun loadBefore(params: LoadParams<Meta>, callback: LoadCallback<Meta, FeedObj>) {}
}
