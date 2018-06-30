package com.grabber.widget.components

import android.content.Context
import android.os.Build
import android.support.graphics.drawable.VectorDrawableCompat
import android.view.View
import android.view.ViewGroup
import java.net.URI

class AndroidUtilities {

    companion object {
        fun getViewsByTag(view: ViewGroup?, tag: String): ArrayList<View> {
            val views = ArrayList<View>()
            view?.let { root ->
                val childCount = root.childCount
                for (i in 0 until childCount) {
                    val child = root.getChildAt(i)
                    if (child is ViewGroup) {
                        views.addAll(getViewsByTag(child, tag))
                    }

                    val tagObj = child.tag
                    if (tagObj != null && tagObj == tag) {
                        views.add(child)
                    }
                }
            }
            return views
        }

        fun hideViewsByTag(root: ViewGroup?, tagName: String) {
            val tags = getViewsByTag(root, tagName)
            for (tag in tags) {
                tag.visibility = View.GONE
            }
        }

        fun showViewsByTag(root: ViewGroup?, tagName: String) {
            val tags = getViewsByTag(root, tagName)
            for (tag in tags) {
                tag.visibility = View.VISIBLE
            }
        }

        fun getDomain(url: String?): String? {
            try {
                val uri = URI(url)
                val domain = uri.host
                return if (domain.startsWith("www.")) domain.substring(4) else domain
            } catch (e: Exception) {
                e.fillInStackTrace()
            }
            return url
        }

        fun removeTags(html: String): String {
            return html
                    .replace("\\s\\s+".toRegex(), "")
                    .replace("\n".toRegex(), "")
                    .replace("&.*?;".toRegex(), "")
                    .replace("<.*?>".toRegex(), "")
        }

        fun setVectorDrawable(context: Context, drawableResId: Int, view: View) {
            val drawable = if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                context.resources.getDrawable(drawableResId, context.theme)
            } else {
                VectorDrawableCompat.create(context.resources, drawableResId, context.theme)
            }
            // view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }
}