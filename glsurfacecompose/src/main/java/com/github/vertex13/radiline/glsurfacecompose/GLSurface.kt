package com.github.vertex13.radiline.glsurfacecompose

import android.opengl.GLSurfaceView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

private enum class Status { INIT, STARTED, STOPPED, DISPOSED }

@Composable
fun GLSurface(
    renderer: GLSurfaceView.Renderer,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val status: MutableState<Status> = remember { mutableStateOf(Status.INIT) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> status.value = Status.STARTED
                Lifecycle.Event.ON_STOP -> status.value = Status.STOPPED
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            status.value = Status.DISPOSED
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            GLSurfaceView(context)
        },
        update = { view ->
            when (status.value) {
                Status.INIT -> {
                    view.setEGLContextClientVersion(3)
                    view.setRenderer(renderer)
                }
                Status.STARTED -> view.onResume()
                Status.STOPPED -> view.onPause()
                Status.DISPOSED -> view.onPause()
            }
        }
    )
}
