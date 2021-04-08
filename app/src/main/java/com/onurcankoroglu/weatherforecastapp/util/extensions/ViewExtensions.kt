package com.onurcankoroglu.weatherforecastapp.util.extensions

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) visible() else gone()
}