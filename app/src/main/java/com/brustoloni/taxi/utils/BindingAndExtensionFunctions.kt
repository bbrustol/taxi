package com.brustoloni.taxi.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("android:src")
fun setSrcVector(view: ImageView, @DrawableRes drawable: Int) {
    view.setImageResource(drawable)
}