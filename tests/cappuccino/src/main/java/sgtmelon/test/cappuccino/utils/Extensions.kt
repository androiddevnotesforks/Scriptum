package sgtmelon.test.cappuccino.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import sgtmelon.extensions.getCalendar
import sgtmelon.extensions.seconds

fun await(time: Long) = runBlocking { delay(time) }

/**
 * Try to escape case when app and test have different minute value.
 *
 * If click happens in the corner seconds (like 0.59) and calendar will be received in
 * the next minute (like 1.10) - this may cause fail of the tests.
 */
fun awaitMinuteEnd() {
    while ((getCalendar().seconds in SAVE_PERIOD).not()) {
        await(time = 1000)
    }
}

private val SAVE_PERIOD = (2..57)