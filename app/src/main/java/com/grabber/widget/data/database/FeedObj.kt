package com.grabber.widget.data.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "feeds", indices = [(Index("name"))])
data class FeedObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "link")
    var link: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "widgetId")
    var widgetId: Int
)