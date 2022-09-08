package sgtmelon.test.common

import java.util.Calendar
import java.util.UUID
import kotlin.random.Random
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText

//region Random functions

fun nextString() = UUID.randomUUID().toString().substring(0, 16)

fun nextShortString() = nextString().substring(0, 4)

fun nextBooleanOrNull() = if (Random.nextBoolean()) Random.nextBoolean() else null

fun nextLongOrNull() = if (Random.nextBoolean()) Random.nextLong() else null

fun nextIntOrNull() = if (Random.nextBoolean()) Random.nextInt() else null

//endregion

fun Int.isDivideEntirely(number: Int = 2): Boolean = this % number == 0

fun Long.isDivideEntirely(number: Long = 2): Boolean = this % number == 0L

//region Time functions

fun getRandomFutureTime(): String {
    return getClearCalendar().apply {
        add(Calendar.MINUTE, (1..60).random())
        add(Calendar.HOUR_OF_DAY, (1..12).random())
        add(Calendar.DAY_OF_YEAR, (10..30).random())
    }.toText()
}

fun getRandomPastTime(): String {
    return getClearCalendar().apply {
        add(Calendar.MINUTE, -(1..60).random())
        add(Calendar.HOUR_OF_DAY, -(1..12).random())
        add(Calendar.DAY_OF_YEAR, -(10..30).random())
    }.toText()
}

//endregion