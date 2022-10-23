package com.github.vertex13.radiline.domain

import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

typealias ObsValueSubscriber<T> = (T) -> Unit

interface ObsValue<T> {
    val value: T
    fun subscribe(s: ObsValueSubscriber<T>): ObsValueSubscriber<T>
    fun unsubscribe(s: ObsValueSubscriber<T>)
}

open class MutableObsValue<T>(initialValue: T) : ObsValue<T> {
    protected val subscribers: MutableSet<ObsValueSubscriber<T>> = HashSet()

    override var value: T by object : ObservableProperty<T>(initialValue) {
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
            return oldValue != newValue
        }

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
            subscribers.forEach { it(newValue) }
        }
    }

    override fun subscribe(s: ObsValueSubscriber<T>): ObsValueSubscriber<T> {
        subscribers.add(s)
        return s
    }

    override fun unsubscribe(s: ObsValueSubscriber<T>) {
        subscribers.remove(s)
    }
}

fun <A, B, R> combineObs(
    a: ObsValue<A>,
    b: ObsValue<B>,
    transformer: (A, B) -> R
): ObsValue<R> {
    return CombinedObsValue2(a, b, transformer)
}

fun <A, B, C, R> combineObs(
    a: ObsValue<A>,
    b: ObsValue<B>,
    c: ObsValue<C>,
    transformer: (A, B, C) -> R
): ObsValue<R> {
    return CombinedObsValue3(a, b, c, transformer)
}

fun <T, R> ObsValue<T>.map(transform: (T) -> R): ObsValue<R> {
    return MappedObsValue(this, transform)
}

private abstract class CombinedObsValue<R>(
    initialValue: R,
    private val obsValues: Array<ObsValue<*>>
) : MutableObsValue<R>(initialValue) {

    override fun subscribe(s: ObsValueSubscriber<R>): ObsValueSubscriber<R> {
        if (subscribers.isEmpty()) {
            obsValues.forEach { it.subscribe(::onUpdate) }
        }
        return super.subscribe(s)
    }

    override fun unsubscribe(s: ObsValueSubscriber<R>) {
        super.unsubscribe(s)
        if (subscribers.isEmpty()) {
            obsValues.forEach { it.unsubscribe(::onUpdate) }
        }
    }

    protected abstract fun onUpdate(any: Any?)
}

private class CombinedObsValue2<A, B, R>(
    private val a: ObsValue<A>,
    private val b: ObsValue<B>,
    private val transform: (A, B) -> R
) : CombinedObsValue<R>(transform(a.value, b.value), arrayOf(a, b)) {
    override fun onUpdate(any: Any?) {
        value = transform(a.value, b.value)
    }
}

private class CombinedObsValue3<A, B, C, R>(
    private val a: ObsValue<A>,
    private val b: ObsValue<B>,
    private val c: ObsValue<C>,
    private val transform: (A, B, C) -> R
) : CombinedObsValue<R>(transform(a.value, b.value, c.value), arrayOf(a, b, c)) {
    override fun onUpdate(any: Any?) {
        value = transform(a.value, b.value, c.value)
    }
}

private class MappedObsValue<T, R>(
    private val from: ObsValue<T>,
    private val transform: (T) -> R
) : MutableObsValue<R>(transform(from.value)) {

    override fun subscribe(s: ObsValueSubscriber<R>): ObsValueSubscriber<R> {
        if (subscribers.isEmpty()) {
            from.subscribe(::onUpdate)
        }
        return super.subscribe(s)
    }

    override fun unsubscribe(s: ObsValueSubscriber<R>) {
        super.unsubscribe(s)
        if (subscribers.isEmpty()) {
            from.unsubscribe(::onUpdate)
        }
    }

    private fun onUpdate(newValue: T) {
        value = transform(newValue)
    }
}
