package sgtmelon.scriptum.basic.extension

import sgtmelon.extension.clearSeconds
import sgtmelon.extension.getText
import java.util.*

fun waitBefore(time: Long, func: () -> Unit = {}) {
    Thread.sleep(time)
    func()
}

fun waitAfter(time: Long, func: () -> Unit) {
    func()
    Thread.sleep(time)
}


/**
 * Add minutes for current time
 */
fun getTime(min: Int): Calendar = Calendar.getInstance().clearSeconds().apply {
    add(Calendar.MINUTE, min)
}