package com.grabber.widget.services

import androidx.work.Data
import androidx.work.Worker
import com.grabber.widget.WidgetApplication
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.data.database.NewsObj
import com.grabber.widget.data.rss.Entry
import com.grabber.widget.data.rss.FeedExecutor
import com.grabber.widget.widgets.WidgetListProvider

class WidgetWorker : Worker(), FeedExecutor.CallBack {

    companion object {
        const val PERIODIC_SYNC = "com.example.android.stackwidget.PERIODIC_SYNC"
        const val ONETIME_SYNC = "com.example.android.stackwidget.ONETIME_SYNC"
    }

    private val mInjector = WidgetApplication.injector()
    private val mFeedRepository = mInjector.getDataRepository()
    private val mFeedParser = mInjector.getFeedParser()
    private val mFeedExecutor = mInjector.getFeedExecutor()

    override fun doWork(): Result {
        // val feed_id = inputData.getLong("FEED_ID", 0)

        mFeedExecutor.feedParser = mFeedParser
        mFeedExecutor.callback = this
        mFeedExecutor.execute()

        outputData = Data.Builder().build()
        return Result.SUCCESS
    }

    override fun getFeeds(): List<FeedObj>? {
        return mFeedRepository.findFeeds()
    }

    override fun onItem(feed: FeedObj, it: Entry) {
        var news = mFeedRepository.findNewsByLink(it.link ?: "", feed.id)
        if (news == null) {
            news = NewsObj(
                    id = 0,
                    description = it.summary ?: "",
                    date = "",
                    label = "",
                    pubDate = "",
                    feedId = feed.id,
                    link = it.link ?: "",
                    title = it.title ?: "",
                    status = "active",
                    type = "default"
            )
        }
        mFeedRepository.saveNews(news)
    }

    override fun onComplete() {
        WidgetListProvider.updateAllWidgets(applicationContext)
    }
}