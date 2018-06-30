package com.grabber.widget.data

import android.arch.lifecycle.MediatorLiveData
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.data.database.NewsObj
import com.grabber.widget.data.database.WidgetDatabase

class DataRepository(val database: WidgetDatabase) {
    private val mObservableProducts = MediatorLiveData<Any>()

    fun findFeedByWidgetId(id: Int): FeedObj? {
        return database.feedDao().findByWidgetId(id)
    }

    fun findVacantFeed(): List<FeedObj>? {
        return database.feedDao().findVacant()
    }

    fun findFeeds(): List<FeedObj>? {
        return database.feedDao().getAll()
    }

    fun findPagedFeeds(limit: Int, offset: Int): List<FeedObj>? {
        return database.feedDao().getPaged(limit, offset)
    }

    fun findNewsByWidgetId(widgetId: Int): List<NewsObj>? {
        val feed = database.feedDao().findByWidgetId(widgetId)
        return database.newsDao().findAllByFeedForWidget(feed.id)
    }

    fun dislikeNews(id: Long): NewsObj? {
        val news = database.newsDao().loadById(id)
        news.status = "disliked"
        database.newsDao().update(news)
        return news
    }

    fun findNewsByLink(link: String, feed_id: Long): NewsObj? {
        return database.newsDao().findByLink(link, feed_id)
    }

    fun saveFeed(feedObj: FeedObj): FeedObj {
        if (feedObj.id <= 0) {
            feedObj.id = database.feedDao().insert(feedObj)
        } else {
            database.feedDao().update(feedObj)
        }
        return feedObj
    }

    fun saveNews(news: NewsObj): NewsObj {
        if (news.id <= 0) {
            news.id = database.newsDao().insert(news)
        } else {
            database.newsDao().update(news)
        }
        return news
    }
}