package sgtmelon.scriptum.infrastructure.utils.extensions

import sgtmelon.test.prod.RunNone

/** Variable only for UI tests. */
@RunNone var maxIndicatorTest = false

const val MAX_COUNT = 99
private const val MAX_COUNT_OVERFLOW = ":D"

fun Int.getIndicatorText(): String {
    return when {
        maxIndicatorTest -> MAX_COUNT_OVERFLOW
        this > MAX_COUNT -> MAX_COUNT_OVERFLOW
        else -> toString()
    }
}