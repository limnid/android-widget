package com.grabber.widget.data.network

import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Request {
    @Throws(IOException::class)
    fun get(urlString: String): InputStream {
        val url = URL(urlString)
        val conn = url.openConnection() as HttpURLConnection
        conn.readTimeout = 10000
        conn.connectTimeout = 15000
        conn.requestMethod = "GET"
        conn.doInput = true
        conn.connect()
        return conn.inputStream
    }
}