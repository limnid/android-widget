package com.grabber.widget.data.database

import android.arch.persistence.room.*

@Entity(
    tableName = "news",
    indices = [
        (Index("pub_date")),
        (Index("feed_id")),
        (Index(value = arrayOf("feed_id", "link"), unique = true))
    ],
    foreignKeys = [
        (ForeignKey(entity = FeedObj::class, parentColumns = arrayOf("id"), childColumns = arrayOf("feed_id")))
    ]
)
data class NewsObj(
    @PrimaryKey(autoGenerate = true)
    var id: Long,

    @ColumnInfo(name = "pub_date")
    var pubDate: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "link")
    var link: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "type")
    var type: String,

    @ColumnInfo(name = "feed_id")
    var feedId: Long,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "label")
    var label: String
)