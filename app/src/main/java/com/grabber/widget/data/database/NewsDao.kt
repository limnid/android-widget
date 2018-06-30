package com.grabber.widget.data.database

import android.arch.persistence.room.*

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAll(): List<NewsObj>

    @Query("SELECT * FROM news WHERE id IN (:id)")
    fun loadAllByIds(id: LongArray): List<NewsObj>

    @Query("SELECT * FROM news WHERE feed_id = :feed_id ORDER BY id DESC LIMIT 50")
    fun findAllByFeed(feed_id: Long): List<NewsObj>

    @Query("SELECT * FROM news WHERE feed_id = :feed_id AND status = 'active' ORDER BY id DESC LIMIT 50")
    fun findAllByFeedForWidget(feed_id: Long): List<NewsObj>

    @Query("SELECT * FROM news WHERE feed_id = :feed_id AND link = :link")
    fun findByFeed(feed_id: Long, link: String?): List<NewsObj>

    @Query("SELECT * FROM news WHERE link LIKE :link AND feed_id = :feed_id LIMIT 1")
    fun findByLink(link: String, feed_id: Long): NewsObj

    @Query("SELECT * FROM news WHERE id = :id")
    fun loadById(id: Long): NewsObj

    @Insert
    fun insertAll(vararg widgetObjs: NewsObj)

    @Insert
    fun insert(widgetObj: NewsObj): Long

    @Update
    fun update(widgetObj: NewsObj): Int

    @Update
    fun updateAll(vararg widgetObj: NewsObj)

    @Delete
    fun delete(widgetObj: NewsObj)
}