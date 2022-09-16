package xyz.skether.radiline.data.preferences

import xyz.skether.radiline.domain.Time

interface Preferences {
    suspend fun getLastTopUpdate(): Time
    suspend fun setLastTopUpdate(time: Time)
}
