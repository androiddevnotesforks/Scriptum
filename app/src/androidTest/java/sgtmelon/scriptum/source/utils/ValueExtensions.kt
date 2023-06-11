package sgtmelon.scriptum.source.utils

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

/**
 * It can't contains only false values. At least need one true.
 */
fun getRandomSignalCheck(initValue: BooleanArray? = null): BooleanArray {
    val melodyValue = Random.nextBoolean()
    val vibrationValue = Random.nextBoolean()

    val resultArray = booleanArrayOf(melodyValue, vibrationValue)

    if (!melodyValue && !vibrationValue) return getRandomSignalCheck(initValue)

    if (initValue != null) {
        if (resultArray.contentEquals(initValue)) {
            return getRandomSignalCheck(initValue)
        }

        assertFalse(initValue.contentEquals(resultArray))
        assertEquals(initValue.size, resultArray.size)
    }

    return resultArray
}