package xyz.skether.radiline.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.skether.radiline.domain.Time
import xyz.skether.radiline.system.AppContext

private const val LAST_TOP_UPDATE = "last_top_update"

class AppSharedPreferences(
    appContext: AppContext,
) : Preferences {

    private val prefs: SharedPreferences = appContext.value.getSharedPreferences(
        "app.pref",
        Context.MODE_PRIVATE
    )

    override suspend fun getLastTopUpdate(): Time = withContext(Dispatchers.IO) {
        Time(prefs.getLong(LAST_TOP_UPDATE, 0L))
    }

    override suspend fun setLastTopUpdate(time: Time) = withContext(Dispatchers.IO) {
        prefs.edit(commit = true) { putLong(LAST_TOP_UPDATE, time.millis) }
    }
}
