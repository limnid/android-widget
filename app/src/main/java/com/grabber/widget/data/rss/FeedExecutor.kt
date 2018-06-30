package com.grabber.widget.data.rss

import android.util.Xml
import com.grabber.widget.data.database.FeedObj
import com.grabber.widget.data.network.Request
import org.xmlpull.v1.XmlPullParser
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class FeedExecutor {
    interface CallBack {
        fun onItem(feed: FeedObj, it: Entry)
        fun getFeeds(): List<FeedObj>?
        fun onComplete()
    }

    var feedParser: FeedParser? = null
    var callback: CallBack? = null

    fun execute() {
        var stream: InputStream? = null
        val parser = Xml.newPullParser()
        val feeds = callback?.getFeeds()

        feeds?.forEach { feed ->
            try {
                stream = Request().get(feed.link)

                val byteOutputStream = ByteArrayOutputStream()
                stream.use { input -> byteOutputStream.use { output -> input?.copyTo(output) } }
                stream?.close()

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                parser.setInput(ByteArrayInputStream(byteOutputStream.toByteArray()), null)
                parser.nextTag()

                feedParser?.readRss(parser)?.forEach {
                    callback?.onItem(feed, it)
                }

                parser.setInput(ByteArrayInputStream(byteOutputStream.toByteArray()), null)
                parser.nextTag()

                feedParser?.readFeed(parser)?.forEach {
                    callback?.onItem(feed, it)
                }

            } catch (e: Exception) {
                e.fillInStackTrace()
            } finally {
                stream?.close()
            }
        }

        callback?.onComplete()
    }
}