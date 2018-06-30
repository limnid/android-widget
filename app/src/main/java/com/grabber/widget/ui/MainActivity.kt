package com.grabber.widget.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.grabber.widget.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        title = getString(R.string.feeds_list)

        val fragment = MainFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
