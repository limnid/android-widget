package com.grabber.widget.presentation.adapters

import android.content.Context
import com.grabber.widget.presentation.paging.LoadingState

interface ListViewAdapter {
    fun setCallbackListener(listener: AdapterListener)
    fun getCallbackListener(): AdapterListener?
    fun getWeakContext(): Context?
    fun setLoadingState(state: LoadingState?)
    fun destroy()
}