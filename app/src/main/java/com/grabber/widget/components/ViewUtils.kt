package com.grabber.widget.components

import android.view.LayoutInflater
import android.view.ViewGroup

class ViewUtils {
    companion object {
        fun inflate(viewGroup: ViewGroup, id: Int) =
                LayoutInflater.from(viewGroup.context).inflate(id, viewGroup, false)
    }
}