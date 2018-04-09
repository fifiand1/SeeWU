package com.wzf.wucarryme.component

import android.content.Context
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide

object ImageLoader {

    fun load(context: Context, @DrawableRes imageRes: Int, view: ImageView) {
        Glide.with(context).load(imageRes).crossFade().into(view)
    }

    fun clear(context: Context) {
        Glide.get(context).clearMemory()
    }
}
