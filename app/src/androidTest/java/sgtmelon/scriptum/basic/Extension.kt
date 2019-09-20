package sgtmelon.scriptum.basic

import sgtmelon.extension.getDateFormat
import java.util.*

fun waitBefore(time: Long, func: () -> Unit) {
    Thread.sleep(time)
    func()
}

fun waitAfter(time: Long, func: () -> Unit) {
    func()
    Thread.sleep(time)
}

fun getFutureTime(): String = getDateFormat().format(
        Date(Calendar.getInstance().timeInMillis + Random().nextLong())
)