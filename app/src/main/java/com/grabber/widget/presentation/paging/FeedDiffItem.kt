package com.grabber.widget.presentation.paging

import android.support.v7.util.DiffUtil
import com.grabber.widget.data.database.FeedObj

class FeedDiffItem : DiffUtil.ItemCallback<FeedObj>() {
    override fun areItemsTheSame(oldItem: FeedObj?, newItem: FeedObj?): Boolean {
        return oldItem?.id == newItem?.id
    }

    override fun areContentsTheSame(oldItem: FeedObj?, newItem: FeedObj?): Boolean {
        return oldItem?.name == newItem?.name && oldItem?.link == newItem?.link
    }
}