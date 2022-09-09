package xyz.skether.radiline

import android.app.Application
import xyz.skether.radiline.dependency.Dependencies
import xyz.skether.radiline.system.AppContext

class RadilineApp : Application() {
    lateinit var dependencies: Dependencies
        private set

    override fun onCreate() {
        super.onCreate()
        dependencies = Dependencies(AppContext.from(this))
    }
}

fun Application.app(): RadilineApp {
    return this as RadilineApp
}
