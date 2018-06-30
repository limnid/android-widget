package com.grabber.widget.presentation.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.grabber.widget.R
import com.grabber.widget.components.AndroidUtilities
import com.grabber.widget.components.ViewUtils
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.presentation.adapters.ListViewAdapterImpl
import kotterknife.bindOptionalView

class ItemHolder(viewGroup: ViewGroup) : BaseHolder(ViewUtils.inflate(viewGroup, R.layout.list_item)) {
    private val container: View? by bindOptionalView(R.id.list_item)
    private val title: TextView? by bindOptionalView(R.id.title)
    private val url: TextView? by bindOptionalView(R.id.url)

    override fun render(adapter: ListViewAdapterImpl, holder: RecyclerView.ViewHolder, position: Int, data: Any?) {
        val feedObj = data as FeedObj?
        title?.text = feedObj?.name
        url?.text = AndroidUtilities.getDomain(feedObj?.link)
        container?.setOnClickListener { adapter.getCallbackListener()?.onItemSelected(position, container) }
    }
}