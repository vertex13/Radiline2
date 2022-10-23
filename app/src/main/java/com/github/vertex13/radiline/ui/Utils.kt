package com.github.vertex13.radiline.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.vertex13.radiline.domain.ObsValue

@Composable
fun <T> ObsValue<T>.asState(): State<T> {
    val state = remember { mutableStateOf(value) }
    DisposableEffect(this) {
        val subscription = subscribe { state.value = it }
        onDispose { unsubscribe(subscription) }
    }
    return state
}
