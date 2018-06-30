package com.grabber.widget.data.database

import android.arch.persistence.room.*

@Dao
interface FeedDao {
    @Query("SELECT * FROM feeds")
    fun getAll(): List<FeedObj>

    @Query("SELECT * FROM feeds ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getPaged(limit: Int, offset: Int): List<FeedObj>

    @Query("SELECT * FROM feeds LIMIT 1")
    fun getFirst(): List<FeedObj>

    @Query("SELECT * FROM feeds WHERE id IN (:id)")
    fun loadAllByIds(id: IntArray): List<FeedObj>

    @Query("SELECT * FROM feeds WHERE name LIKE :name LIMIT 1")
    fun findByTitle(name: String): List<FeedObj>

    @Query("SELECT * FROM feeds WHERE widgetId = :widgetId")
    fun findByWidgetId(widgetId: Int): FeedObj

    @Query("SELECT * FROM feeds WHERE widgetId = 0")
    fun findVacant(): List<FeedObj>

    @Insert
    fun insertAll(vararg feedObjs: FeedObj)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feedObj: FeedObj): Long

    @Update
    fun update(feedObj: FeedObj): Int

    @Update
    fun updateAll(vararg feedObj: FeedObj)

    @Delete
    fun delete(feedObj: FeedObj)

    @Query("SELECT * FROM feeds WHERE widgetId != 0 AND status = 'active'")
    fun getAllSynchronous(): List<FeedObj>
}