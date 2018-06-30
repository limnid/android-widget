package com.grabber.widget.presentation.adapters

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.presentation.holders.BaseHolder
import com.grabber.widget.presentation.holders.ItemHolder
import com.grabber.widget.presentation.holders.ProgressHolder
import com.grabber.widget.presentation.paging.FeedDiffItem
import com.grabber.widget.presentation.paging.LoadingState
import java.lang.ref.WeakReference

class ListViewAdapterImpl :
        PagedListAdapter<FeedObj, RecyclerView.ViewHolder>(FeedDiffItem()), ListViewAdapter {

    private var lastPosition = -1
    private var weakContext: WeakReference<Context>? = null
    private var mListener: AdapterListener? = null
    var showLoader: Boolean = false

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AlphaAnimation(0.0f, 1.0f)
            animation.duration = 300
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        weakContext = WeakReference(viewGroup.context)
        return when (viewType) {
            AdapterItem.PROGRESS -> ProgressHolder(viewGroup)
            else -> ItemHolder(viewGroup)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProgressHolder) {
            holder.render(this, holder, position, null)
        } else if (holder is BaseHolder) {
            holder.render(this, holder, position, getItem(position))
            setAnimation(holder.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemId(position: Int): Long {
        if (position == itemCount - 1) {
            return -1
        }
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            return AdapterItem.PROGRESS
        }
        return AdapterItem.ITEM
    }

    override fun setLoadingState(state: LoadingState?) {
        showLoader = state == LoadingState.LOADING
        notifyItemChanged(itemCount)
    }

    override fun setCallbackListener(listener: AdapterListener) { mListener = listener }
    override fun getCallbackListener(): AdapterListener? = mListener
    override fun getWeakContext(): Context? = weakContext?.get()
    override fun destroy() { mListener = null }
}