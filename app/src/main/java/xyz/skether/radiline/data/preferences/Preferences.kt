package xyz.skether.radiline.data.preferences

import xyz.skether.radiline.domain.Time
import xyz.skether.radiline.domain.TuneInBase

interface Preferences {
    fun getLastTopUpdate(): Time
    fun setLastTopUpdate(time: Time)

    fun getTuneInBase(): TuneInBase
    fun setTuneInBase(tuneInBase: TuneInBase)
}
