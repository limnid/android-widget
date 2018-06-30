package com.grabber.widget.presentation.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.grabber.widget.R
import com.grabber.widget.components.ViewUtils
import com.grabber.widget.presentation.adapters.ListViewAdapterImpl
import kotterknife.bindOptionalView

class ProgressHolder(viewGroup: ViewGroup) : BaseHolder(ViewUtils.inflate(viewGroup, R.layout.list_progress)) {
    private val progressBar: View? by bindOptionalView(R.id.progressBar)

    override fun render(adapter: ListViewAdapterImpl, holder: RecyclerView.ViewHolder, position: Int, data: Any?) {
        if (adapter.showLoader) {
            progressBar?.visibility = View.VISIBLE
        } else {
            progressBar?.visibility = View.GONE
        }
    }
}