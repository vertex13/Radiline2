package xyz.skether.radiline.data.backend.type

import xyz.skether.radiline.domain.TuneInBase

class TuneInXml(
    override val pls: String?,
    override val m3u: String?,
    override val xspf: String?,
) : TuneInBase
