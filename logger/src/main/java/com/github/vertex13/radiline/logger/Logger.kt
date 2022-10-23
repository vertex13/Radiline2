package com.github.vertex13.radiline.logger

private lateinit var currentLogger: Logger

fun initLogger(logger: Logger) {
    currentLogger = logger
}

fun currentLogger(): Logger = currentLogger

interface Logger {
    fun verbose(tag: String, message: String, throwable: Throwable? = null)
    fun debug(tag: String, message: String, throwable: Throwable? = null)
    fun info(tag: String, message: String, throwable: Throwable? = null)
    fun warning(tag: String, message: String, throwable: Throwable? = null)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}
