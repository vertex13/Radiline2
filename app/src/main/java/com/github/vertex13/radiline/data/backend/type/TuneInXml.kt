package com.github.vertex13.radiline.data.backend.type

import com.github.vertex13.radiline.domain.TuneInBase

class TuneInXml(
    override val pls: String?,
    override val m3u: String?,
    override val xspf: String?,
) : TuneInBase
