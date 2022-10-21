package xyz.skether.logger

class EmptyLogger : Logger {
    override fun verbose(tag: String, message: String, throwable: Throwable?) = Unit
    override fun debug(tag: String, message: String, throwable: Throwable?) = Unit
    override fun info(tag: String, message: String, throwable: Throwable?) = Unit
    override fun warning(tag: String, message: String, throwable: Throwable?) = Unit
    override fun error(tag: String, message: String, throwable: Throwable?) = Unit
}
