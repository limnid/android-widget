package com.grabber.widget.widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RemoteViews
import com.grabber.widget.R
import com.grabber.widget.WidgetApplication
import com.grabber.widget.ui.WidgetActivity

class WidgetListProvider : AppWidgetProvider() {

    companion object {
        const val NEXT_ACTION = "com.example.android.stackwidget.NEXT_ACTION"
        const val PREV_ACTION = "com.example.android.stackwidget.PREV_ACTION"
        const val CLICK_ACTION = "com.example.android.stackwidget.CLICK_ACTION"
        const val DISLIKE_ACTION = "com.example.android.stackwidget.DISLIKE_ACTION"
        const val UPDATE_WIDGET = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        const val POSITION = "com.example.android.stackwidget.POSITION"
        const val ITEM_ID = "com.example.android.stackwidget.ITEM_ID"

        /**
         * Update all widgets
         * */
        fun updateAllWidgets(context: Context?) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val currentWidget = ComponentName(context, WidgetListProvider::class.java)
            val allWidgetIds = appWidgetManager.getAppWidgetIds(currentWidget)
            for (id in allWidgetIds) {
                context?.let {
                    onUpdateWidget(it, appWidgetManager, id)
                }
            }
        }

        /**
         * Create widget intent
         * */
        fun createUpdateIntent(bundle: Bundle): Intent? {
            val widgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            val widgetIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            return widgetIntent
        }

        private fun onUpdateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, WidgetListService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val views = RemoteViews(context.packageName, R.layout.widget_list)

            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val actionIntent = Intent(context, WidgetListProvider::class.java)
            actionIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            val nextPendingIntent = PendingIntent.getBroadcast(context, appWidgetId, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, nextPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.stack_view)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val viewIndex = intent.getIntExtra(POSITION, 0)
        val viewItemId = intent.getLongExtra(ITEM_ID, 0)
        val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
        val appWidgetManager = AppWidgetManager.getInstance(context)

        when(intent.action) {
            NEXT_ACTION -> {
                val views = RemoteViews(context.packageName, R.layout.widget_list)
                views.showNext(R.id.stack_view)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
            PREV_ACTION -> {
                val views = RemoteViews(context.packageName, R.layout.widget_list)
                views.showPrevious(R.id.stack_view)
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
            UPDATE_WIDGET -> {
                onUpdateWidget(context, appWidgetManager, appWidgetId)
            }
            CLICK_ACTION -> {
                val activityIntent = Intent(context, WidgetActivity::class.java)
                activityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activityIntent.putExtras(intent)
                context.startActivity(activityIntent)
            }
            DISLIKE_ACTION -> {
                WidgetApplication.injector()
                    .getDataRepository()
                    .dislikeNews(viewItemId)

                onUpdateWidget(context, appWidgetManager, appWidgetId)
            }
        }
        
        super.onReceive(context, intent)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val currentWidget = ComponentName(context, WidgetListProvider::class.java)
        val allWidgetIds = appWidgetManager.getAppWidgetIds(currentWidget)
        for (i in allWidgetIds) {
            onUpdateWidget(context, appWidgetManager, i)
        }
    }
}