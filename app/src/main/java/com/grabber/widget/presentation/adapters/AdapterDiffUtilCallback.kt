package com.grabber.widget.presentation.adapters

import android.support.v7.util.DiffUtil
import android.util.SparseArray
import com.grabber.widget.data.database.FeedObj


class AdapterDiffUtilCallback(
        private val oldList: SparseArray<AdapterItem?>,
        private val newList: SparseArray<AdapterItem?>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size()
    }

    override fun getNewListSize(): Int {
        return newList.size()
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]?.data as FeedObj
        val new = newList[newItemPosition]?.data as FeedObj
        return old.id == new.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]?.data as FeedObj
        val new = newList[newItemPosition]?.data as FeedObj
        return old.name == new.name && old.link === new.link
    }
}