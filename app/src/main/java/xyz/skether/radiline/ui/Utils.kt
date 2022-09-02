package xyz.skether.radiline.ui

import androidx.compose.runtime.*
import xyz.skether.radiline.domain.ObsValue

@Composable
fun <T> ObsValue<T>.asState(): State<T> {
    val state = remember { mutableStateOf(value) }
    DisposableEffect(this) {
        val subscription = subscribe { state.value = it }
        onDispose { unsubscribe(subscription) }
    }
    return state
}
