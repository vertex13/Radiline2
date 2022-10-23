package com.github.vertex13.radiline.logger

import android.util.Log

class LogcatLogger : Logger {
    override fun verbose(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) {
            Log.v(tag, message)
        } else {
            Log.v(tag, message, throwable)
        }
    }

    override fun debug(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) {
            Log.d(tag, message)
        } else {
            Log.d(tag, message, throwable)
        }
    }

    override fun info(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) {
            Log.i(tag, message)
        } else {
            Log.i(tag, message, throwable)
        }
    }

    override fun warning(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) {
            Log.w(tag, message)
        } else {
            Log.w(tag, message, throwable)
        }
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        if (throwable == null) {
            Log.e(tag, message)
        } else {
            Log.e(tag, message, throwable)
        }
    }
}
