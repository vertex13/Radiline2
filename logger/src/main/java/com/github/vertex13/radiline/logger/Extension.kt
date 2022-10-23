package com.github.vertex13.radiline.logger

private const val ANON_TAG = "anonymous_class_name"

fun Any.logV(message: String, throwable: Throwable? = null, tag: String? = null) {
    currentLogger().verbose(tag ?: this::class.simpleName ?: ANON_TAG, message, throwable)
}

fun Any.logD(message: String, throwable: Throwable? = null, tag: String? = null) {
    currentLogger().debug(tag ?: this::class.simpleName ?: ANON_TAG, message, throwable)
}

fun Any.logI(message: String, throwable: Throwable? = null, tag: String? = null) {
    currentLogger().info(tag ?: this::class.simpleName ?: ANON_TAG, message, throwable)
}

fun Any.logW(message: String, throwable: Throwable? = null, tag: String? = null) {
    currentLogger().warning(tag ?: this::class.simpleName ?: ANON_TAG, message, throwable)
}

fun Any.logE(message: String, throwable: Throwable? = null, tag: String? = null) {
    currentLogger().error(tag ?: this::class.simpleName ?: ANON_TAG, message, throwable)
}
