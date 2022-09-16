package xyz.skether.radiline.system

import xyz.skether.radiline.domain.CurrentTime
import xyz.skether.radiline.domain.Time

class SystemCurrentTime : CurrentTime {
    override fun invoke(): Time {
        return Time(System.currentTimeMillis())
    }
}
