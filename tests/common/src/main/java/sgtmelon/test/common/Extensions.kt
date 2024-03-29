package sgtmelon.test.common

import org.junit.Assert.assertNotEquals
import sgtmelon.extensions.emptyString
import sgtmelon.extensions.getClearCalendar
import sgtmelon.extensions.toText
import sgtmelon.extensions.uniqueId
import java.util.Calendar
import kotlin.random.Random

//region Random functions

fun getRandomSize(minSize: Int = 5): Int = (minSize..(minSize * 2)).random()

fun nextString() = uniqueId.substring(0, 16)

fun nextString(times: Int) = nextString().repeat(times)

fun nextShortString() = nextString().substring(0, 4)

fun nextStringOrEmpty() = if (halfChance()) nextString() else emptyString()

fun nextBooleanOrNull() = if (halfChance()) Random.nextBoolean() else null

fun nextLongOrNull() = if (halfChance()) Random.nextLong() else null

fun nextIntOrNull() = if (halfChance()) Random.nextInt() else null

/** 50% TRUE - 50% FALSE */
fun halfChance() = Random.nextBoolean()

/** 25% TRUE - 75% FALSE */
fun oneFourthChance() = halfChance() && halfChance()

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

private class SizeException : IllegalArgumentException("Values must have size greater than 1")

fun <E> Collection<E>.getDifferentValues(setValue: E? = null): Pair<E, E> {
    if (size <= 1) throw SizeException()

    return getDifferentValues(setValue) { random() }
}

fun <E> Array<E>.getDifferentValues(setValue: E? = null): Pair<E, E> {
    if (size <= 1) throw SizeException()

    return getDifferentValues(setValue) { random() }
}

inline fun <E> getDifferentValues(setValue: E?, getRandom: () -> E): Pair<E, E> {
    val firstValue = setValue ?: getRandom()
    var secondValue = getRandom()
    while (secondValue == firstValue) {
        secondValue = getRandom()
    }

    assertNotEquals(firstValue, secondValue)

    return firstValue to secondValue
}