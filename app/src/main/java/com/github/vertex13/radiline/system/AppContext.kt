package com.github.vertex13.radiline.system

import android.content.Context

@JvmInline
value class AppContext private constructor(val value: Context) {
    companion object {
        fun from(context: Context): AppContext = AppContext(context.applicationContext)
    }
}
