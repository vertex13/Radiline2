package com.github.vertex13.radiline.data.preferences

import com.github.vertex13.radiline.domain.Time
import com.github.vertex13.radiline.domain.TuneInBase

interface Preferences {
    fun getLastTopUpdate(): Time
    fun setLastTopUpdate(time: Time)

    fun getTuneInBase(): TuneInBase
    fun setTuneInBase(tuneInBase: TuneInBase)
}
