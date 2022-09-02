package xyz.skether.radiline.domain

import kotlin.properties.Delegates

typealias ObsValueSubscriber<T> = (T) -> Unit

interface ObsValue<T> {
    val value: T
    fun subscribe(s: ObsValueSubscriber<T>): ObsValueSubscriber<T>
    fun unsubscribe(s: ObsValueSubscriber<T>)
}

open class MutableObsValue<T>(initialValue: T) : ObsValue<T> {
    private val subscribers: MutableSet<ObsValueSubscriber<T>> = HashSet()

    override var value: T by Delegates.observable(initialValue) { _, _, newValue ->
        subscribers.forEach { it(newValue) }
    }

    override fun subscribe(s: ObsValueSubscriber<T>): ObsValueSubscriber<T> {
        subscribers.add(s)
        return s
    }

    override fun unsubscribe(s: ObsValueSubscriber<T>) {
        subscribers.remove(s)
    }
}

private class CombinedObsValue3<A, B, C, R>(
    private val a: ObsValue<A>,
    private val b: ObsValue<B>,
    private val c: ObsValue<C>,
    private val transform: (A, B, C) -> R
) : MutableObsValue<R>(transform(a.value, b.value, c.value)), ObsValue<R> {
    private val obsArray: Array<ObsValue<*>> = arrayOf(a, b, c)

    override fun subscribe(s: ObsValueSubscriber<R>): ObsValueSubscriber<R> {
        obsArray.forEach { it.subscribe(::onUpdate) }
        return super.subscribe(s)
    }

    override fun unsubscribe(s: ObsValueSubscriber<R>) {
        super.unsubscribe(s)
        obsArray.forEach { it.unsubscribe(::onUpdate) }
    }

    private fun onUpdate(any: Any?) {
        value = transform(a.value, b.value, c.value)
    }
}

fun <A, B, C, R> combineObs(
    a: ObsValue<A>,
    b: ObsValue<B>,
    c: ObsValue<C>,
    transformer: (A, B, C) -> R
): ObsValue<R> {
    return CombinedObsValue3(a, b, c, transformer)
}
