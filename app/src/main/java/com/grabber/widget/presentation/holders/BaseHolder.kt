package com.grabber.widget.presentation.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.grabber.widget.presentation.adapters.ListViewAdapterImpl

open class BaseHolder(var view: View) : RecyclerView.ViewHolder(view) {
    open fun render(adapter: ListViewAdapterImpl, holder: RecyclerView.ViewHolder, position: Int, data: Any?) {}
    open fun onViewDetachedFromWindow(adapter: ListViewAdapterImpl, holder: RecyclerView.ViewHolder?) {}
    open fun onViewAttachedToWindow(adapter: ListViewAdapterImpl, holder: RecyclerView.ViewHolder?) {}
}