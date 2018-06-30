package com.grabber.widget.components

import android.content.Context
import android.graphics.Typeface

class Fonts(context: Context) {
    private var fonts: MutableMap<Int, Typeface>? = null

    companion object {
        const val AWESOME = 1
    }

    init {
        val fontAwesome = Typeface.createFromAsset(context.assets, "fonts/FontAwesome.ttf")
        fonts = mutableMapOf()
        fonts?.put(AWESOME, fontAwesome)
    }

    fun get(type: Int): Typeface? {
        return fonts?.get(type)
    }
}