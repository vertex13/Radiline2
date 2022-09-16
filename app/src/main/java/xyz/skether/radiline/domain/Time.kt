package xyz.skether.radiline.domain

@JvmInline
value class Time(val millis: Long) {
    fun before(time: Time): Boolean = millis < time.millis
    fun after(time: Time): Boolean = millis > time.millis
    operator fun plus(time: Time): Time = Time(millis + time.millis)
}
