package com.github.vertex13.radiline.system

import com.github.vertex13.radiline.domain.CurrentTime
import com.github.vertex13.radiline.domain.Time

class SystemCurrentTime : CurrentTime {
    override fun invoke(): Time {
        return Time(System.currentTimeMillis())
    }
}
