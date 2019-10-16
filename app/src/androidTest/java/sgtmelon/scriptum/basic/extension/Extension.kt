package sgtmelon.scriptum.basic.extension

import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getString
import java.util.*

fun waitBefore(time: Long, func: () -> Unit = {}) {
    Thread.sleep(time)
    func()
}

fun waitAfter(time: Long, func: () -> Unit) {
    func()
    Thread.sleep(time)
}

fun getFutureTime(): String = Calendar.getInstance().clearSeconds().apply {
    add(Calendar.MINUTE, (1..60).random())
    add(Calendar.HOUR_OF_DAY, (1..12).random())
    add(Calendar.DAY_OF_YEAR, (10..30).random())
}.getString()

/**
 * Add minutes for current time
 */
fun getTime(minute: Int): Calendar = Calendar.getInstance().clearSeconds().apply {
    add(Calendar.MINUTE, minute)
}