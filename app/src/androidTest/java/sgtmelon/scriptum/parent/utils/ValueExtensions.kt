package sgtmelon.scriptum.parent.utils

import kotlin.random.Random

/**
 * It can't contains only false values. At least need one true.
 */
fun getRandomSignalCheck(): BooleanArray {
    val melodyValue = Random.nextBoolean()
    val vibrationValue = Random.nextBoolean()

    if (!melodyValue && !vibrationValue) return getRandomSignalCheck()

    return booleanArrayOf(melodyValue, vibrationValue)
}