package sgtmelon.test.cappuccino.utils

import java.util.Calendar
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import sgtmelon.extensions.getCalendar

fun await(time: Long) = runBlocking { delay(time) }

/** Try to escape cases when app and test have different minute value. */
fun awaitMinuteEnd() {
    while (getCalendar().get(Calendar.SECOND) > 50) {
        await(time = 1000)
    }
}
