package xyz.skether.radiline.data.preferences.type

import xyz.skether.radiline.domain.TuneInBase

class TuneInBasePrefs(
    override val pls: String?,
    override val m3u: String?,
    override val xspf: String?,
) : TuneInBase
