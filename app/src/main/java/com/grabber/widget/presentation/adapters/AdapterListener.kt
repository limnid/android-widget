package com.grabber.widget.presentation.adapters

import android.view.View

interface AdapterListener {
    fun onItemSelected(position: Int, view: View? = null)
}