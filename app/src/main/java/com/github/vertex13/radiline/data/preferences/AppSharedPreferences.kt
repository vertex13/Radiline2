package com.github.vertex13.radiline.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.github.vertex13.radiline.data.preferences.type.TuneInBasePrefs
import com.github.vertex13.radiline.domain.Time
import com.github.vertex13.radiline.domain.TuneInBase
import com.github.vertex13.radiline.system.AppContext

private const val LAST_TOP_UPDATE = "last_top_update"
private const val TUNE_IN_BASE_PLS = "tune_in_base_pls"
private const val TUNE_IN_BASE_M3U = "tune_in_base_m3u"
private const val TUNE_IN_BASE_XSPF = "tune_in_base_xspf"

class AppSharedPreferences(
    appContext: AppContext,
) : Preferences {

    private val prefs: SharedPreferences by lazy {
        appContext.value.getSharedPreferences("app.pref", Context.MODE_PRIVATE)
    }

    override fun getLastTopUpdate(): Time {
        return Time(prefs.getLong(LAST_TOP_UPDATE, 0L))
    }

    override fun setLastTopUpdate(time: Time) {
        prefs.edit { putLong(LAST_TOP_UPDATE, time.millis) }
    }

    override fun getTuneInBase(): TuneInBase {
        return TuneInBasePrefs(
            pls = prefs.getString(TUNE_IN_BASE_PLS, null),
            m3u = prefs.getString(TUNE_IN_BASE_M3U, null),
            xspf = prefs.getString(TUNE_IN_BASE_XSPF, null),
        )
    }

    override fun setTuneInBase(tuneInBase: TuneInBase) {
        prefs.edit {
            putString(TUNE_IN_BASE_PLS, tuneInBase.pls)
            putString(TUNE_IN_BASE_M3U, tuneInBase.m3u)
            putString(TUNE_IN_BASE_XSPF, tuneInBase.xspf)
        }
    }
}
