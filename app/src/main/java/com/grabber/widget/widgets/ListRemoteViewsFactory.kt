package com.grabber.widget.widgets

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.grabber.widget.R
import com.grabber.widget.WidgetApplication
import com.grabber.widget.components.AndroidUtilities

open class ListRemoteViewsFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val mWidgetItems = ArrayList<ListRemoteViewItem>()
    private val mAppWidgetId: Int = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)

    override fun onCreate() {}

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(mContext.packageName, R.layout.widget_item)

        if (mWidgetItems.size > 0) {
            views.setTextViewText(R.id.widget_title, mWidgetItems[position].title)
            views.setTextViewText(R.id.widget_description, AndroidUtilities.removeTags(mWidgetItems[position].description))

            val extras = Bundle()
            extras.putInt(WidgetListProvider.POSITION, position)
            extras.putLong(WidgetListProvider.ITEM_ID, mWidgetItems[position].id)

            /**
             * Next button
             * */
            val nextIntent = Intent()
            nextIntent.putExtras(extras)
            nextIntent.action = WidgetListProvider.NEXT_ACTION
            views.setOnClickFillInIntent(R.id.next, nextIntent)

            /**
             * Prev button
             * */
            val prevIntent = Intent()
            prevIntent.putExtras(extras)
            prevIntent.action = WidgetListProvider.PREV_ACTION
            views.setOnClickFillInIntent(R.id.prev, prevIntent)

            /**
             * Configuration
             * */
            val clickIntent = Intent()
            clickIntent.putExtras(extras)
            clickIntent.action = WidgetListProvider.CLICK_ACTION
            views.setOnClickFillInIntent(R.id.widget_title, clickIntent)

            /**
             * Dislike button
             * */
            val dislikeIntent = Intent()
            dislikeIntent.putExtras(extras)
            dislikeIntent.action = WidgetListProvider.DISLIKE_ACTION
            views.setOnClickFillInIntent(R.id.dislike, dislikeIntent)
        }

        return views
    }

    override fun onDestroy() {
        mWidgetItems.clear()
    }

    override fun getCount(): Int {
        return mWidgetItems.size
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        val newsList = WidgetApplication
                .injector()
                .getDataRepository()
                .findNewsByWidgetId(mAppWidgetId)

        newsList?.forEach {
            mWidgetItems.add(ListRemoteViewItem(it.id, it.title, it.description))
        }
    }
}