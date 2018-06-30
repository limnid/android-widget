package com.grabber.widget.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(NewsObj::class), (FeedObj::class)], version = 1)
abstract class WidgetDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun feedDao(): FeedDao
}