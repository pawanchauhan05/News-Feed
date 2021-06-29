package com.app.newsfeed.utilities

import android.view.View

fun View.visibility(flag : Boolean) : Unit {
    visibility = if(flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
